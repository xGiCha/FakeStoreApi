package gr.android.fakestoreapi.ui.detalis

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.ui.BaseViewModelImpl
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo
import gr.android.fakestoreapi.ui.emitAsync
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase
) : BaseViewModelImpl<ProductDetailsContract.State, ProductDetailsContract.Event>() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<ProductDetailsContract.State?>(null)
    private val _productId = MutableStateFlow<Int?>(null)
    private val _products = MutableSharedFlow<List<ProductDomainModel>?>(replay = 1)

    init {
        getProducts()
    }

    private val result: StateFlow<ProductDetailsContract.State?> =
        combine(
            _products,
            _productId
        ) { products, productId ->
            val product = products?.find  { it.id == productId }

            ProductDetailsContract.State.Data(
                productDetailsScreenInfo = ProductDetailsScreenInfo(
                    title = R.string.product_details_title,
                    toolBarInfo = ToolBarInfo(
                        toolbarLeftIcon = R.drawable.ic_left_arrow,
                        toolMiddleIcon = R.drawable.ic_toolbar,
                        toolRightIcon = R.drawable.ic_edit_product,
                        toolLeftIconVisibility = true,
                        toolMiddleIconVisibility = false,
                        toolRightIconVisibility = true
                    )
                ),
                product = product?.let {
                    ProductDetailsContract.State.Data.Product (
                        category = it.category.orEmpty(),
                        description = it.description.orEmpty(),
                        id = it.id ?: -1,
                        image = it.image.orEmpty(),
                        price = (it.price ?: 0.0).toString() + " €",
                        title = it.title.orEmpty()
                    )
                },
            )
        }.onEach {
            isLoading.value = false
            error.value = null
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override val uiState: StateFlow<ProductDetailsContract.State?>
        get() = combine(isLoading, result, error) { isLoading, result, error ->
            val response = if (error != null) {
                ProductDetailsContract.State.Error(error)
            } else if (isLoading) {
                ProductDetailsContract.State.Loading
            } else {
                result ?: ProductDetailsContract.State.Error("No info retrieved")
            }
            lastState.value = response
            response
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            lastState.value
        )

    fun getProducts() {
        viewModelScope.launch {
            productsUseCase.invoke().collectLatest {
                when(it){
                    is Outcome.Error -> {
                        error.emit(it.message)
                    }
                    is Outcome.Loading -> {}
                    is Outcome.Success -> _products.emit(it.data)
                }
            }
        }
    }

    fun setProduct(productId: Int?) {
        viewModelScope.launch {
            _productId.emit(productId)
        }
    }
    fun onBack(){
        events.emitAsync(ProductDetailsContract.Event.OnBack)
    }
    fun navigateToDetailsScreen(){
        events.emitAsync(ProductDetailsContract.Event.NavigateToEditProductScreen)
    }

}