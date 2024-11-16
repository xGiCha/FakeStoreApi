package gr.android.fakestoreapi.ui.edit

import androidx.compose.runtime.Stable

interface UpdateProductContract {

    sealed interface Event {
        data object OnBack: Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val productDetailsScreenInfo: UpdateProductScreenInfo,
            val product: Product?,
        ): State {

            data class UpdateProductScreenInfo(
                val title: Int,
                val price: Int,
                val category: Int,
                val description: Int
            )

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