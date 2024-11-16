package gr.android.fakestoreapi.ui.login

import app.cash.turbine.test
import gr.android.fakestoreapi.BaseTest
import gr.android.fakestoreapi.CoroutineTestRule
import gr.android.fakestoreapi.domain.usecases.LoginUseCase
import gr.android.fakestoreapi.utils.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class LoginViewModelTest: BaseTest() {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var loginUseCase: LoginUseCase

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        loginViewModel = LoginViewModel(loginUseCase)
        coEvery { loginUseCase.invoke("Tom", "Black") } returns flowOf (Outcome.Success(Unit))
    }

    @Test
    fun `GIVEN correct credentials WHEN login screen THEN navigate to home screen`() = runTest {
        val givenUserName = "Tom"
        val giveLastName = "Black"
        val events = loginViewModel.events.testSubscribe()
        every { loginUseCase.invoke(givenUserName, giveLastName) } returns flowOf (Outcome.Success(Unit))
        loginViewModel.login(givenUserName, giveLastName)
        events.assertLast {
            assertEquals(
                LoginContract.Event.NavigateToHomeScreen,
                it
            )
        }.dispose()
    }

    @Test
    fun `GIVEN wrong credentials WHEN login screen THEN error appears`() = runTest {
        val givenUserName = "Tom"
        val giveLastName = "Black"
        every { loginUseCase.invoke(givenUserName, giveLastName) } returns flowOf (Outcome.Error("Error"))
        loginViewModel.login(givenUserName, giveLastName)
        loginViewModel.uiState.test {
            assertEquals(
                expected = "Error",
                actual = (awaitItem() as? LoginContract.State.Data)?.screenInfo?.errorMessage
            )
        }
    }
}