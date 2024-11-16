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
import gr.android.fakestoreapi.common.annotation.Application
import gr.android.fakestoreapi.data.local.SessionPreferences
import gr.android.fakestoreapi.data.network.services.LoginApi
import gr.android.fakestoreapi.data.repositories.LogoutRepositoryImpl
import gr.android.fakestoreapi.data.repositories.login.LoginRepositoryImpl
import gr.android.fakestoreapi.domain.repository.LoginRepository
import gr.android.fakestoreapi.domain.repository.LogoutRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLogoutRepository(
        @Application coroutineScope: CoroutineScope,
        sessionPreferences: SessionPreferences
    ): LogoutRepository {
        return LogoutRepositoryImpl(
            coroutineScope = coroutineScope,
            sessionPreferences = sessionPreferences
        )
    }

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
