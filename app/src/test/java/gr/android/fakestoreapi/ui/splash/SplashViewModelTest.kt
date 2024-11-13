package gr.android.fakestoreapi.ui.splash

import app.cash.turbine.test
import gr.android.fakestoreapi.CoroutineTestRule
import gr.android.fakestoreapi.domain.usecases.SilentLoginUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SplashViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var silentLoginUseCase: SilentLoginUseCase

    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        splashViewModel = SplashViewModel(silentLoginUseCase)
        coEvery { silentLoginUseCase.isLoggedIn() } returns flowOf(false)
    }

    @Test
    fun `GIVEN first time login WHEN open the app THEN navigate to login screen`() = runTest {
        splashViewModel = SplashViewModel(silentLoginUseCase)
        splashViewModel.events.test {
            assertEquals(
                SplashContract.Event.NavigateToLoginScreen,
                (awaitItem() as? SplashContract.Event)
            )
        }
    }

    @Test
    fun `GIVEN already loggedIn user WHEN open the app THEN navigate to Home screen`() = runTest {
        coEvery { silentLoginUseCase.isLoggedIn() } returns flowOf(true)
        splashViewModel.events.test {
            assertEquals(
                SplashContract.Event.NavigateToHomeScreen,
                (awaitItem() as? SplashContract.Event)
            )
        }
    }
}