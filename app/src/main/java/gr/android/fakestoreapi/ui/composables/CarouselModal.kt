package gr.android.fakestoreapi.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CarouselModal(
    modifier: Modifier = Modifier,
    item: @Composable (PagerState, Int) -> Unit,
    size: Int,
    onPageChanged: ((Int) -> Unit)? = null,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp), // Add horizontal padding here
    spacedBy: Dp = 0.dp,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    fitPagerContentToParent: Boolean = false,
    userScrollEnabled: () -> Boolean = { true },
    pagerState: PagerState = rememberPagerState(pageCount = { size }),
) {
    var activePage by rememberSaveable { mutableStateOf(1) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            if (userScrollEnabled() && pagerState.currentPage < size) {
                val nextPage = (pagerState.currentPage + 1) % size
                coroutineScope.launch {
                    pagerState.animateScrollToPage(nextPage)
                }
                activePage = nextPage + 1
                onPageChanged?.invoke(nextPage)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            activePage = page + 1
            onPageChanged?.invoke(page)
        }
    }

    Column(modifier = modifier.background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerModifier = if (fitPagerContentToParent) Modifier.weight(1f) else Modifier
        HorizontalPager(
            state = pagerState,
            modifier = pagerModifier
                .fillMaxWidth(),
            verticalAlignment = alignment,
            pageSpacing = spacedBy,
            contentPadding = paddingValues,
            userScrollEnabled = userScrollEnabled()
        ) { index ->
            item(pagerState, index)
        }
        if (size > 1) {
            Spacer(modifier = Modifier.height(17.dp))
            CarouselDots(
                pages = size,
                activePage = activePage,
            )
        }
    }
}