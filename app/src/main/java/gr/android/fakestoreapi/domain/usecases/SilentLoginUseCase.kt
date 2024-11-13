package gr.android.fakestoreapi.domain.usecases

import gr.android.fakestoreapi.domain.repository.LoginRepository
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SilentLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    fun isLoggedIn(): Flow<Boolean> {
        return flow {
            val result = loginRepository.isLoggedIn()
            when(result) {
                is Outcome.Success -> {
                    emit(true)
                }
                else -> {
                    emit(false)
                }
            }
        }
    }
}