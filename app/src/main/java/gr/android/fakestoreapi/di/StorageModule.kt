package gr.android.fakestoreapi.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.android.fakestoreapi.data.local.database.FakeStoreApiDatabase
import gr.android.fakestoreapi.data.local.database.products.categories.ProductCategoriesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): FakeStoreApiDatabase {
        return Room.databaseBuilder(
            app,
            FakeStoreApiDatabase::class.java,
            "products_table.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductsDao(database: FakeStoreApiDatabase) = database.dao

    @Provides
    @Singleton
    fun provideProductCategoriesDao(database: FakeStoreApiDatabase) = database.productCategoriesDao
}
