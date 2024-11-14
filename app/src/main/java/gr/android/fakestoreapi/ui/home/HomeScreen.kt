package gr.android.fakestoreapi.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.android.fakestoreapi.R
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

                }
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
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        HomeScreenInfo(
            allFeaturedTitle = "test",
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
        onSearch = {}
    )
}