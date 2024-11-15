package gr.android.fakestoreapi.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gr.android.fakestoreapi.ui.theme.IconGray
import gr.android.fakestoreapi.ui.theme.Pink

@Composable
fun CarouselDots(
    pages: Int,
    activePage: Int,
    modifier : Modifier? = null
) {
    Row(
        modifier = modifier ?: Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (page in 1..pages) {
            Dot(
                isActive = (page == activePage),
            )
        }
    }
}

@Composable
private fun Dot(
    isActive: Boolean,
) {
    val color by animateColorAsState(
        targetValue = if (isActive) Pink else IconGray,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing
        )
    )
    val width by animateDpAsState(
        targetValue = 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 300f
        )
    )
    Box(modifier = Modifier
        .clip(RoundedCornerShape(100.dp))
        .height(8.dp)
        .width(width)
        .background(color)
    )
}

@Preview
@Composable
private fun PreviewCarouselDots() {
    CarouselDots(
        pages = 2,
        activePage = 1,
    )
}