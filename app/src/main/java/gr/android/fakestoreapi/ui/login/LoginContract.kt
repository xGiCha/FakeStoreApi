package gr.android.fakestoreapi.ui.login

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

interface LoginContract {

    sealed interface Event {
        data object NavigateToHomeScreen : Event
    }

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        @Stable
        data class Data(
            val screenInfo: ScreenInfo,
        ): State {

            data class ScreenInfo(
                val loginTitle: Int,
                val userNameField: Int,
                val passwordField: Int,
                val buttonText: Int,
                val userIcon: Int,
                val lockIcon: Int,
                val visibilityIcon: ImageVector,
                val visibilityOffIcon: ImageVector,
                val errorMessage: String
            )
        }
    }
}