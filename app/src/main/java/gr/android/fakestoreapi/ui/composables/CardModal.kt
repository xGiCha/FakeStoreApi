package gr.android.fakestoreapi.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.theme.white

@Composable
fun CardModal(
    image: String
) {

    Column {
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
                            .data(data = image)
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
}