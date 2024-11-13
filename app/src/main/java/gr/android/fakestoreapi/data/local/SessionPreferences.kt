package gr.android.fakestoreapi.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import gr.android.fakestoreapi.utils.Constants.AUTH_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = accessToken
        }
    }

    val getAccessToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[AUTH_KEY] ?: ""
    }

    suspend fun clearAccessToken() {
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = ""
        }
    }
}