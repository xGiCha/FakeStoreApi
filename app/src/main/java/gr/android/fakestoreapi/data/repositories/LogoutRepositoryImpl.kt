package gr.android.fakestoreapi.data.repositories

import gr.android.fakestoreapi.data.local.SessionPreferences
import gr.android.fakestoreapi.domain.repository.LogoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LogoutRepositoryImpl(
    private val sessionPreferences: SessionPreferences,
    private val coroutineScope: CoroutineScope,
): LogoutRepository {

    override suspend fun logout() {
        coroutineScope.launch {
            sessionPreferences.clearAccessToken()
        }
    }
}