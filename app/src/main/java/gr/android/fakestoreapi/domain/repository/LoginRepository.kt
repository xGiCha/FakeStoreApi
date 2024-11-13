package gr.android.fakestoreapi.domain.repository

import gr.android.fakestoreapi.data.network.models.login.LoginRequest
import gr.android.fakestoreapi.utils.Outcome

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Outcome<Unit?>
    suspend fun isLoggedIn(): Outcome<Boolean>
}