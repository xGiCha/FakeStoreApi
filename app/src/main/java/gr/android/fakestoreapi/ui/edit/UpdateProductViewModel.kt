package gr.android.fakestoreapi.ui.edit

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.ui.BaseViewModelImpl
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo
import gr.android.fakestoreapi.ui.edit.UpdateProductContract.State.Data.UpdateProductScreenInfo
import gr.android.fakestoreapi.ui.emitAsync
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProductViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase
) : BaseViewModelImpl<UpdateProductContract.State, UpdateProductContract.Event>() {

    private val refreshFLow: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)
    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<UpdateProductContract.State?>(null)
    private val _productId = MutableStateFlow<Int?>(null)
    private val _products = MutableSharedFlow<List<ProductDomainModel>?>(replay = 1)

    init {
        refresh()
    }

    fun refresh() {
        refreshFLow.tryEmit(Unit)
        getProducts()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val result: StateFlow<UpdateProductContract.State?> =
        refreshFLow.flatMapLatest {
            combine(
                _products,
                _productId
            ) { products, productId ->
                val product = products?.find { it.id == productId }

                UpdateProductContract.State.Data(
                    productDetailsScreenInfo = UpdateProductScreenInfo(
                        title = R.string.update_title,
                        price = R.string.update_price,
                        category = R.string.update_category,
                        description = R.string.update_description
                    ),
                    product = product?.let {
                        UpdateProductContract.State.Data.Product(
                            category = it.category.orEmpty(),
                            description = it.description.orEmpty(),
                            id = it.id ?: -1,
                            image = it.image.orEmpty(),
                            price = (it.price ?: 0.0).toString() + " €",
                            title = it.title.orEmpty()
                        )
                    },
                )
            }
        }.onEach {
            isLoading.value = false
            error.value = null
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override val uiState: StateFlow<UpdateProductContract.State?>
        get() = combine(isLoading, result, error) { isLoading, result, error ->
            val response = if (error != null) {
                UpdateProductContract.State.Error(error)
            } else if (isLoading) {
                UpdateProductContract.State.Loading
            } else {
                result ?: UpdateProductContract.State.Error("No info retrieved")
            }
            lastState.value = response
            response
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            lastState.value
        )

    private fun getProducts() {
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

    fun updateProduct(
        id: Int?,
        title: String?,
        price: String?,
        description: String?,
        image: String?,
        category: String?,
    ) {
        viewModelScope.launch {
            productsUseCase.updateProduct(
                id = id ?: -1,
                title = title.orEmpty(),
                price = if(!price.isNullOrEmpty()) price.toDouble() else 0.0,
                description = description.orEmpty(),
                image = image.orEmpty(),
                category = category.orEmpty()
            ).collectLatest {
                when(it) {
                    is Outcome.Error -> {
                        error.value = it.message
                    }
                    is Outcome.Loading -> {}
                    is Outcome.Success -> {
                        onBack()
                    }
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
        events.emitAsync(UpdateProductContract.Event.OnBack)
    }

}