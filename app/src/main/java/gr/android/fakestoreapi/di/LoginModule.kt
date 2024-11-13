package gr.android.fakestoreapi.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gr.android.fakestoreapi.data.local.SessionPreferences
import gr.android.fakestoreapi.data.network.LoginApi
import gr.android.fakestoreapi.data.repositories.LoginRepositoryImpl
import gr.android.fakestoreapi.domain.repository.LoginRepository
import gr.android.fakestoreapi.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginApi: LoginApi,
        sessionPreferences: SessionPreferences
    ): LoginRepository {
        return LoginRepositoryImpl(
            loginApi = loginApi,
            sessionPreferences = sessionPreferences
        )
    }

    @Provides
    @Singleton
    fun provideSessionPreferences(dataStore: DataStore<Preferences>): SessionPreferences {
        return SessionPreferences(dataStore)
    }

    @Provides
    @Singleton
    fun providePreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("PREFERENCES")
            }
        )
}
