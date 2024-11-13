package gr.android.fakestoreapi.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.domain.usecases.LoginUseCase
import gr.android.fakestoreapi.ui.BaseViewModelImpl
import gr.android.fakestoreapi.ui.emitAsync
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
 private val loginUseCase: LoginUseCase
) : BaseViewModelImpl<LoginContract.State, LoginContract.Event>() {

    private val refreshFlow: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)
    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val lastState = MutableStateFlow<LoginContract.State?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            refreshFlow.emit(Unit)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val result: StateFlow<LoginContract.State?> =
        refreshFlow.flatMapLatest {
            combine(
                _errorMessage
            ) { (error) ->
                LoginContract.State.Data(
                    screenInfo = LoginContract.State.Data.ScreenInfo(
                        loginTitle = R.string.login_title,
                        buttonText = R.string.login_btn_text,
                        passwordField = R.string.password_field,
                        userNameField = R.string.user_name_field,
                        userIcon = R.drawable.ic_user,
                        lockIcon = R.drawable.ic_lock,
                        visibilityIcon = Icons.Default.Visibility,
                        visibilityOffIcon = Icons.Default.VisibilityOff,
                        errorMessage = error.orEmpty()
                    )
                )
            }
        }.onEach {
            isLoading.value = false
            error.value = null
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override val uiState: StateFlow<LoginContract.State?>
        get() = combine(isLoading, result, error) { isLoading, result, error ->
            val response = if (error != null) {
                LoginContract.State.Error(error)
            } else if (isLoading) {
                LoginContract.State.Loading
            } else {
                result ?: LoginContract.State.Error("No info retrieved")
            }
            lastState.value = response
            response
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            lastState.value
        )

    private fun navigateToHomeScreen() {
        events.emitAsync(LoginContract.Event.NavigateToHomeScreen)
    }

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            loginUseCase.invoke(username = userName, password = password).collectLatest {
                when(it){
                    is Outcome.Error -> {
                        _errorMessage.emit(it.message)
                    }
                    is Outcome.Loading -> {}
                    is Outcome.Success -> {
                        navigateToHomeScreen()
                    }
                }
            }
        }
    }
}