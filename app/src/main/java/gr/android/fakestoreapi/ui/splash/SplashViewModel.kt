package gr.android.fakestoreapi.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.BaseViewModel
import gr.android.fakestoreapi.ui.BaseViewModelImpl
import gr.android.fakestoreapi.ui.emitAsync
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(

) : BaseViewModelImpl<SplashContract.State, SplashContract.Event>() {

    private val _uiState = MutableStateFlow<SplashContract.State>(
        SplashContract.State.Data(splashDrawable = R.drawable.ic_splash_logo)
    )

    init {
        // Trigger navigation after 3 seconds
        viewModelScope.launch {
            delay(3000)
            events.emitAsync(SplashContract.Event.NavigateToHomeScreen)
        }
    }

    override val uiState: StateFlow<SplashContract.State?>
        get() = _uiState
}