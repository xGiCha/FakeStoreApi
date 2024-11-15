package gr.android.fakestoreapi.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.composables.CarouseItemModal
import gr.android.fakestoreapi.ui.composables.CarouselModal
import gr.android.fakestoreapi.ui.composables.CategoryItemModal
import gr.android.fakestoreapi.ui.composables.ProductHorizontalList
import gr.android.fakestoreapi.ui.composables.SearchModal
import gr.android.fakestoreapi.ui.composables.TopBarModal
import gr.android.fakestoreapi.ui.home.HomeContract.State.Data.HomeScreenInfo

sealed interface HomeNavigation {
    data class NavigateToDetails(val productId: Int): HomeNavigation
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigate: (HomeNavigation) -> Unit
) {

    LaunchedEffect(homeViewModel.events) {
        homeViewModel.events.collect { event ->
            when(event){
                is HomeContract.Event.NavigateToDetailsScreen -> {
                    navigate(HomeNavigation.NavigateToDetails(event.productId))
                }
            }
        }
    }

    when(val state = homeViewModel.uiState.collectAsStateWithLifecycle().value) {
        is HomeContract.State.Data -> {
            HomeScreenContent(
                homeScreenInfo = state.homeScreenInfo,
                onSearchTextChange = {
                    homeViewModel.setSearchText(it)
                },
                onSearch = {},
                categories = state.categories,
                onCategorySelected = {
                    homeViewModel.selectedCategory(it)
                },
                selectedCategory = state.selectedCategory,
                carouselItems = state.carouselItems,
                products = state.products,
                navigate = {
                    when(it){
                        is HomeNavigation.NavigateToDetails -> {
                            homeViewModel.navigateToDetails(it.productId)
                        }
                    }
                }
            )
        }
        else -> {}
    }
}

@Composable
private fun HomeScreenContent(
    homeScreenInfo: HomeScreenInfo,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    selectedCategory: String,
    carouselItems: List<Pair<Int?, String>>,
    products: Map<String, List<HomeContract.State.Data.Product>>?,
    navigate: (HomeNavigation) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
    ) {
        TopBarModal(
            leftIconVisibility = homeScreenInfo.toolbarInfo.toolLeftIconVisibility
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))
                SearchModal(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onSearchTextChange = onSearchTextChange,
                    onSearch = onSearch
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = homeScreenInfo.allFeaturedTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(25.dp))
                CategoryItemModal(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    categories = categories,
                    categorySelected = selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(25.dp))
                CarouselModal(
                    item = { pagerState, index ->
                        CarouseItemModal(
                            item = carouselItems[index],
                            onClick = {
                                navigate(HomeNavigation.NavigateToDetails(it))
                            }
                        )
                    },
                    size = carouselItems.size,
                    paddingValues = PaddingValues(horizontal = 16.dp), // Horizontal padding at the start
                    spacedBy = 16.dp
                )
            }

            items(categories) { category ->
                val productsInCategory = products?.get(category)
                if (productsInCategory?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.fillMaxWidth().height(25.dp))
                    ProductHorizontalList(
                        productsInCategory = productsInCategory,
                        onProductClick = {
                            navigate(HomeNavigation.NavigateToDetails(it.id))
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(40.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {

    val products = listOf(
        HomeContract.State.Data.Product(
            "All",
            id = 5,
            title = "Woman Printed Kurta",
            image = "",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            price = "1500"
        ))

    val dummyProducts = products.groupBy { it.category }

    HomeScreenContent(
        HomeScreenInfo(
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
        onSearchTextChange = {},
        onSearch = {},
        categories = listOf("electronics", "clothes"),
        onCategorySelected = {},
        selectedCategory = "electronics",
        carouselItems = listOf(Pair(1, "https://performance.ford.com/content/fordracing/home/performance-vehicles/_jcr_content/par/fr_external_link_com_522722112/image.img.jpg/1682003426508.jpg")),
        products = dummyProducts,
        navigate = {}
    )
}