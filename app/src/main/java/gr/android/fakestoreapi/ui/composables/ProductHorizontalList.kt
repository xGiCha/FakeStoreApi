package gr.android.fakestoreapi.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gr.android.fakestoreapi.ui.home.HomeContract

@Composable
fun ProductHorizontalList(
    productsInCategory: List<HomeContract.State.Data.Product>,
    onProductClick: (HomeContract.State.Data.Product) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(productsInCategory) { product ->
            ProductItemModal(
                product = product,
                onProductClick = onProductClick
            )
        }
    }
}

@Preview
@Composable
private fun ProductHorizontalListPreview() {
    ProductHorizontalList(
        productsInCategory = listOf(
            HomeContract.State.Data.Product(
                "All",
                id = 5,
                title = "Woman Printed Kurta",
                image = "",
                description = "Neque porro quisquam est qui dolorem ipsum quia",
                price = "1500"
            ),
            HomeContract.State.Data.Product(
                "All",
                id = 5,
                title = "Woman Printed Kurta",
                image = "",
                description = "Neque porro quisquam est qui dolorem ipsum quia",
                price = "1500"
            )
        ),
        onProductClick = {},
    )
}