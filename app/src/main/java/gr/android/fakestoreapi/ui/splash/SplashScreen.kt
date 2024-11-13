package gr.android.fakestoreapi.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

sealed interface SplashNavigation {
    data object NavigateToHome: SplashNavigation
    data object LoginToHome: SplashNavigation
}

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigate: (SplashNavigation) -> Unit
) {

    LaunchedEffect(splashViewModel.events) {
      splashViewModel.events.collect { event ->
          when(event){
              SplashContract.Event.NavigateToHomeScreen -> {
                  navigate(SplashNavigation.NavigateToHome)
              }

              SplashContract.Event.NavigateToLoginScreen -> {
                  navigate(SplashNavigation.LoginToHome)
              }
          }
      }
    }

    when(val state = splashViewModel.uiState.collectAsStateWithLifecycle().value) {
        is SplashContract.State.Data -> {
            SplashScreenContent(
                splashDrawable = state.splashDrawable
            )
        }
        else -> {}
    }

}

@Composable
private fun SplashScreenContent(
    splashDrawable: Int
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = splashDrawable), // Replace with your actual drawable resource
            contentDescription = null,
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        navigate = {}
    )
}