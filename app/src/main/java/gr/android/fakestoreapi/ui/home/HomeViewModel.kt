package gr.android.fakestoreapi.ui.home

import androidx.lifecycle.viewModelScope
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.domain.usecases.CategoriesUseCase
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.ui.BaseViewModelImpl
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
    private val _selectedCategory = MutableStateFlow<String?>(All_ITEMS)

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
            _searchText,
            _selectedCategory
        ).mapLatest { (error, categories, products, searchText, selectedCategory) ->

            val productImages = products?.map { it.id to it.image.orEmpty() }
            val productList = products?.mapNotNull {
                it.takeIf { product -> product.category != null && product.title != null }?.let {
                    HomeContract.State.Data.Product(
                        category = it.category.orEmpty(),
                        description = it.description.orEmpty(),
                        id = it.id ?: -1,
                        image = it.image.orEmpty(),
                        price = (it.price ?: 0.0).toString() + " €",
                        title = it.title.orEmpty()
                    )
                }
            }.orEmpty()

            // Filter products based on selected category and searchText
            val filteredProducts = productList
                .filter { product ->
                    (selectedCategory == All_ITEMS || product.category == selectedCategory) &&
                            (searchText.isNullOrEmpty() || product.title.contains(searchText, ignoreCase = true))
                }

            // Group filtered products by category
            val productsByCategory = filteredProducts.groupBy { it.category }

            HomeContract.State.Data(
                homeScreenInfo = HomeScreenInfo(
                    allFeaturedTitle = "All Featured",
                    toolbarInfo = HomeScreenInfo.ToolBarInfo(
                        toolbarLeftIcon = R.drawable.ic_left_arrow,
                        toolMiddleIcon = R.drawable.ic_toolbar,
                        toolRightIcon = R.drawable.ic_profile,
                        toolLeftIconVisibility = false,
                        toolMiddleIconVisibility = true,
                        toolRightIconVisibility = true
                    )
                ),
                searchText = searchText.orEmpty(),
                categories = categories.orEmpty(),
                selectedCategory = selectedCategory.orEmpty(),
                carouselItems = productImages.orEmpty(),
                products = productsByCategory
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
        if(text.isNotEmpty())
            selectedCategory(All_ITEMS)
        _searchText.value = text
    }

    fun selectedCategory(category: String) {
        setSearchText("")
        _selectedCategory.value = category
    }

    fun navigateToDetails(productId: Int){
        events.emitAsync(HomeContract.Event.NavigateToDetailsScreen(productId = productId))
    }

    companion object {
        const val All_ITEMS = "All"
    }
}