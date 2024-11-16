package gr.android.fakestoreapi.ui.detalis

import androidx.compose.runtime.Stable

interface ProductDetailsContract {

    sealed interface Event {
        data object NavigateToEditProductScreen : Event
        data object OnBack: Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val productDetailsScreenInfo: ProductDetailsScreenInfo,
            val product: Product?,
        ): State {

            data class ProductDetailsScreenInfo(
                val title: Int,
                val toolBarInfo: ToolBarInfo,
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