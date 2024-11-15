package gr.android.fakestoreapi.ui.detalis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.composables.TopBarModal
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo
import gr.android.fakestoreapi.ui.theme.white

sealed interface ProductDetailsScreenNavigation {
    data object OnBack : ProductDetailsScreenNavigation
}

@Composable
fun ProductDetailsScreen(
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel(),
    productId: Int?,
    navigate: (ProductDetailsScreenNavigation) -> Unit
) {
    productDetailsViewModel.setProduct(productId = productId)

    LaunchedEffect(productDetailsViewModel.events) {
        productDetailsViewModel.events.collect {
            when (it) {
                ProductDetailsContract.Event.NavigateToEditProductScreen -> {}
                ProductDetailsContract.Event.OnBack -> {
                    navigate(ProductDetailsScreenNavigation.OnBack)
                }
            }
        }
    }
    when (val state = productDetailsViewModel.uiState.collectAsStateWithLifecycle().value) {
        is ProductDetailsContract.State.Data -> {
            ProductDetailsScreenContent(
                product = state.products,
                productDetailsScreenInfo = state.productDetailsScreenInfo,
                navigate = {
                    when (it) {
                        ProductDetailsScreenNavigation.OnBack -> {
                            productDetailsViewModel.onBack()
                        }
                    }
                }
            )
        }

        is ProductDetailsContract.State.Loading -> {}
        else -> {

        }
    }
}

@Composable
fun ProductDetailsScreenContent(
    product: ProductDetailsContract.State.Data.Product?,
    productDetailsScreenInfo: ProductDetailsScreenInfo,
    navigate: (ProductDetailsScreenNavigation) -> Unit,
) {
    var isExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBarModal(
            middleIconVisibility = productDetailsScreenInfo.toolBarInfo.toolMiddleIconVisibility,
            rightIcon = productDetailsScreenInfo.toolBarInfo.toolRightIcon,
            rightIconRoundedCorners = RoundedCornerShape(0.dp),
            onBackClick = {
                navigate(ProductDetailsScreenNavigation.OnBack)
            }
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(213.dp),

                ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(white),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = product?.image)
                                .apply(block = fun ImageRequest.Builder.() {
                                    placeholder(R.drawable.ic_placeholder)
                                    crossfade(true)
                                }).build()
                        ),
                        contentScale = ContentScale.Fit,
                        contentDescription = ""
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 55.dp),
                text = product?.title.orEmpty(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = product?.category.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = product?.price.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(productDetailsScreenInfo.title),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = product?.description.orEmpty(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    maxLines = if (isExpanded.value) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (!isExpanded.value) {
                Text(
                    text = stringResource(R.string.read_more),
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { isExpanded.value = !isExpanded.value }
                )
                    }
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp))
        }
    }
}

@Preview
@Composable
private fun ProductDetailsScreenContentPreview() {
    val product = ProductDetailsContract.State.Data.Product(
        "All",
        id = 5,
        title = "Woman Printed Kurta",
        image = "",
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = "1500 €"
    )
    ProductDetailsScreenContent(
        product = product,
        productDetailsScreenInfo = ProductDetailsScreenInfo(
            title = R.string.product_details_title,
            toolBarInfo = ToolBarInfo(
                toolbarLeftIcon = R.drawable.ic_left_arrow,
                toolMiddleIcon = R.drawable.ic_toolbar,
                toolRightIcon = R.drawable.ic_edit_product,
                toolLeftIconVisibility = true,
                toolMiddleIconVisibility = false,
                toolRightIconVisibility = true
            )
        ),
        navigate = {}
    )
}