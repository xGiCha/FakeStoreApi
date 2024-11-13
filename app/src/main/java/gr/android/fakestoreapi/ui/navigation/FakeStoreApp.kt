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
import gr.android.fakestoreapi.ui.home.HomeScreen
import gr.android.fakestoreapi.ui.login.LoginNavigation
import gr.android.fakestoreapi.ui.login.LoginScreen
import gr.android.fakestoreapi.ui.splash.SplashNavigation
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
                    navigateToSplashScreen(navController = navController)
                    navigateToLoginScreen(navController = navController)
                    navigateToHomeScreen(navController = navController)
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
        SplashScreen(
            navigate = {
                when(it){
                    SplashNavigation.LoginToHome -> {
                        navController.navigate(Screen.LoginScreen.route.value) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }

                    }
                    SplashNavigation.NavigateToHome -> {
                        goHomeAndClearBackStack(navController)
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.navigateToLoginScreen(
    navController: NavHostController
) {
    composable(route = Screen.LoginScreen.route.value) {
        LoginScreen(
            navigate = {
                when(it){
                    LoginNavigation.NavigateToHome -> {
                        goHomeAndClearBackStack(navController)
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.navigateToHomeScreen(
    navController: NavHostController
) {
    composable(route = Screen.HomeScreen.route.value) {
        HomeScreen()
    }
}

private fun goHomeAndClearBackStack(navController: NavHostController) {
    navController.navigate(Screen.HomeScreen.route.value) {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }
}