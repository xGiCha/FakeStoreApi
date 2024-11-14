package gr.android.fakestoreapi.ui.home

import androidx.compose.runtime.Stable
import gr.android.fakestoreapi.R

interface HomeContract {

    sealed interface Event {
        data object NavigateToDetailsScreen : Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val homeScreenInfo: HomeScreenInfo,
            val searchText: String? = null,
        ): State {
            data class HomeScreenInfo(
                val allFeaturedTitle: String,
                val toolbarInfo: ToolBarInfo
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
        }
    }
}