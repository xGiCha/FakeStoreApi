package gr.android.fakestoreapi.data.repositories

import gr.android.fakestoreapi.data.local.SessionPreferences
import gr.android.fakestoreapi.data.network.LoginApi
import gr.android.fakestoreapi.data.network.models.login.LoginRequest
import gr.android.fakestoreapi.domain.repository.LoginRepository
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class LoginRepositoryImpl(
    private val loginApi: LoginApi,
    private val sessionPreferences: SessionPreferences
): LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Outcome<Unit?> {
        return try {
            val result = loginApi.loginUser(loginRequest)
            if (result.token.isNotEmpty()){
                sessionPreferences.saveAccessToken(result.token)
                Outcome.Success(Unit)
            } else {
                Outcome.Error(message = "Login failed. Please check your credentials and try again.")
            }
        } catch (e: IOException) {
            Outcome.Error(message = "Unable to connect to the server. Please check your internet connection and try again.")
        } catch (e: HttpException) {
            Outcome.Error(message = "Unexpected error occurred. Please try again later.")
        }
    }

    override suspend fun isLoggedIn(): Outcome<Boolean> {
        return if (sessionPreferences.getAccessToken.first().isEmpty()) {
            Outcome.Error("Error")
        } else {
            Outcome.Success(true)
        }
    }
}