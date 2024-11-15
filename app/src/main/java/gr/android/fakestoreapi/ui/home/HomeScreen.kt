package gr.android.fakestoreapi.ui.home

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import gr.android.fakestoreapi.ui.composables.SearchModal
import gr.android.fakestoreapi.ui.composables.TopBarModal
import gr.android.fakestoreapi.ui.home.HomeContract.State.Data.HomeScreenInfo

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    when(val state = homeViewModel.uiState.collectAsStateWithLifecycle().value) {
        is HomeContract.State.Data -> {
            HomeScreenContent(
                homeScreenInfo = state.homeScreenInfo,
                currentSearchText = state.searchText,
                onSearchTextChange = {
                    homeViewModel.setSearchText(it)
                },
                onSearch = {

                },
                categories = state.categories,
                onCategorySelected = {
                    homeViewModel.selectedCategory(it)
                },
                selectedCategory = state.selectedCategory,
                carouselItems = state.carouselItems.orEmpty()
            )
        }
        else -> {}
    }
}

@Composable
private fun HomeScreenContent(
    homeScreenInfo: HomeScreenInfo,
    currentSearchText: String?,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    selectedCategory: String,
    carouselItems: List<String>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        TopBarModal(
            leftIconVisibility = homeScreenInfo.toolbarInfo.toolLeftIconVisibility
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))

        SearchModal(
            currentSearchText = currentSearchText,
            onSearchTextChange = onSearchTextChange,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = homeScreenInfo.allFeaturedTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(25.dp))
                CategoryItemModal(
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
                            imageUrl = carouselItems[index]
                        )
                    },
                    size = carouselItems.size,
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
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
        currentSearchText = "",
        onSearchTextChange = {},
        onSearch = {},
        categories = listOf("electronics", "clothes"),
        onCategorySelected = {},
        selectedCategory = "electronics",
        carouselItems = listOf("https://performance.ford.com/content/fordracing/home/performance-vehicles/_jcr_content/par/fr_external_link_com_522722112/image.img.jpg/1682003426508.jpg")
    )
}