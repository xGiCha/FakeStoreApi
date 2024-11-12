package gr.android.fakestoreapi.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gr.android.fakestoreapi.ui.splash.SplashScreen

@Composable
fun FakeStoreApp() {
    val navController = rememberNavController()
    FakeStoreNavHost(
        navController = navController
    )
}

@Composable
fun FakeStoreNavHost(
    navController: NavHostController,
) {
    Scaffold(
    content = { insets ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        top = 0.dp,
                        bottom = insets.calculateBottomPadding(),
                        start = insets.calculateStartPadding(LocalLayoutDirection.current),
                        end = insets.calculateEndPadding(LocalLayoutDirection.current)
                    )
                ),
        ) {
            Box {
                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route.value,
                ) {
                    navigateToSplashScreen(navController)
                }
            }
        }
    }
    )

}

private fun NavGraphBuilder.navigateToSplashScreen(
    navController: NavHostController
) {
    composable(route = Screen.SplashScreen.route.value) {
        SplashScreen()
    }
}