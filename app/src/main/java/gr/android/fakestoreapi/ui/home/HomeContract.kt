package gr.android.fakestoreapi.ui.home

import androidx.compose.runtime.Stable

interface HomeContract {

    sealed interface Event {
        data class NavigateToDetailsScreen(val productId: Int) : Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val homeScreenInfo: HomeScreenInfo,
            val searchText: String? = null,
            val categories: List<String>,
            val selectedCategory: String,
            val carouselItems: List<Pair<Int?, String>>,
            val products: Map<String, List<Product>>?,
        ): State {
            data class HomeScreenInfo(
                val allFeaturedTitle: String,
                val toolbarInfo: ToolBarInfo,
            ) {
                data class ToolBarInfo(
                    val toolbarLeftIcon: Int,
                    val toolMiddleIcon: Int,
                    val toolRightIcon: Int,
                    val toolLeftIconVisibility: Boolean,
                    val toolMiddleIconVisibility: Boolean,
                    val toolRightIconVisibility: Boolean
                )
            }

            data class Product(
                val category: String,
                val description: String,
                val id: Int,
                val image: String,
                val price: String,
                val title: String
            )
        }
    }
}