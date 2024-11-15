package gr.android.fakestoreapi.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.theme.white

@Composable
fun CarouseItemModal(
    imageUrl: String
) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(189.dp),

    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(white),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = imageUrl)
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
}

@Preview
@Composable
private fun CarouselItemModalPreview() {
    CarouseItemModal(
        imageUrl = "https://performance.ford.com/content/fordracing/home/performance-vehicles/_jcr_content/par/fr_external_link_com_522722112/image.img.jpg/1682003426508.jpg"
    )
}