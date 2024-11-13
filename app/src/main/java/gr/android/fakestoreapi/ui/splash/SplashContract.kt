package gr.android.fakestoreapi.ui.splash

import androidx.compose.runtime.Stable

interface SplashContract {

    sealed interface Event {
        data object NavigateToHomeScreen : Event
        data object NavigateToLoginScreen : Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val splashDrawable: Int
        ): State
    }
}