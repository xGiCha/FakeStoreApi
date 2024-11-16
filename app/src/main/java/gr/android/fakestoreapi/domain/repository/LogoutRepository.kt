package gr.android.fakestoreapi.domain.repository

interface LogoutRepository {
    suspend fun logout()
}