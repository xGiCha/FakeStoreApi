package gr.android.fakestoreapi.domain.usecases

import gr.android.fakestoreapi.data.network.models.login.LoginRequest
import gr.android.fakestoreapi.domain.repository.LoginRepository
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    fun invoke(
        username: String,
        password: String,
    ): Flow<Outcome<Unit?>> {
        val validationOutcome = validateCredentials(username, password)
        if (validationOutcome is Outcome.Error) {
            return flow { emit(validationOutcome) }
        }

        val loginRequest = LoginRequest(
            username = username.trim(),
            password = password.trim()
        )

        return flow {
            emit(Outcome.Loading())
            val result = loginRepository.login(loginRequest)
            emit(result)
        }
    }

    private fun validateCredentials(username: String, password: String): Outcome<Unit> {
        return when {
            username.isBlank() -> Outcome.Error("Username cannot be blank")
            password.isBlank() -> Outcome.Error("Password cannot be blank")
            else -> Outcome.Success(Unit)
        }
    }
}