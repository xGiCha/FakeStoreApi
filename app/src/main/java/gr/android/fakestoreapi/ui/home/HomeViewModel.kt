package gr.android.fakestoreapi.ui.home

import androidx.lifecycle.viewModelScope
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.domain.usecases.CategoriesUseCase
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.ui.BaseViewModelImpl
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
import gr.android.fakestoreapi.ui.home.HomeContract.State.Data.HomeScreenInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
    private val productsUseCase: ProductsUseCase
) : BaseViewModelImpl<HomeContract.State, HomeContract.Event>() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<HomeContract.State?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _productCategories = MutableSharedFlow<List<String>?>(replay = 1)
    private val _products = MutableSharedFlow<List<ProductDomainModel>?>(replay = 1)
    private val _searchText = MutableStateFlow<String?>("")

    init {
        getProductCategories()
        getProducts()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val result: StateFlow<HomeContract.State?> =
        combineTuple(
            _errorMessage,
            _productCategories,
            _products,
            _searchText
        ).mapLatest { (error, categories, products, searchText) ->
            HomeContract.State.Data(
                homeScreenInfo = HomeScreenInfo(
                    allFeaturedTitle = "test",
                    toolbarInfo = HomeScreenInfo.ToolBarInfo(
                        toolbarLeftIcon = R.drawable.ic_left_arrow,
                        toolMiddleIcon = R.drawable.ic_toolbar,
                        toolRightIcon = R.drawable.ic_profile,
                        toolLeftIconVisibility = true,
                        toolMiddleIconVisibility = true,
                        toolRightIconVisibility = true
                    )
                ),
                searchText = searchText.orEmpty()
            )
        }.onEach {
            isLoading.value = false
            error.value = null
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override val uiState: StateFlow<HomeContract.State?>
        get() = combine(isLoading, result, error) { isLoading, result, error ->
            val response = if (error != null) {
                HomeContract.State.Error(error)
            } else if (isLoading) {
                HomeContract.State.Loading
            } else {
                result ?: HomeContract.State.Error("No info retrieved")
            }
            lastState.value = response
            response
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            lastState.value
        )

    fun getProductCategories() {
        viewModelScope.launch {
            categoriesUseCase.invoke().collectLatest {
                when(it){
                    is Outcome.Error -> {
                        _errorMessage.emit(it.message)
                    }
                    is Outcome.Loading -> {}
                    is Outcome.Success -> _productCategories.emit(it.data)
                }
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            productsUseCase.invoke().collectLatest {
                when(it){
                    is Outcome.Error -> {
                        _errorMessage.emit(it.message)
                    }
                    is Outcome.Loading -> {}
                    is Outcome.Success -> _products.emit(it.data)
                }
            }
        }
    }

    fun setSearchText(text: String) {
        _searchText.value = text
    }
}