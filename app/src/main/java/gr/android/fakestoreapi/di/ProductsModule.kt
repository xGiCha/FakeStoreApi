package gr.android.fakestoreapi.di

import gr.android.fakestoreapi.common.annotation.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.android.fakestoreapi.data.local.database.products.ProductsDataSource
import gr.android.fakestoreapi.data.local.database.products.categories.ProductCategoriesDao
import gr.android.fakestoreapi.data.network.services.ProductsApi
import gr.android.fakestoreapi.data.repositories.products.ProductCategoriesRepositoryImpl
import gr.android.fakestoreapi.data.repositories.products.ProductsRepositoryImpl
import gr.android.fakestoreapi.domain.repository.ProductCategoriesRepository
import gr.android.fakestoreapi.domain.repository.ProductsRepository
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    fun provideProductCategoriesRepository(
        productsApi: ProductsApi,
        productCategoriesDao: ProductCategoriesDao,
        @Application coroutineScope: CoroutineScope,
    ): ProductCategoriesRepository {
        return ProductCategoriesRepositoryImpl(
            productsApi = productsApi,
            productCategoriesDao = productCategoriesDao,
            coroutineScope = coroutineScope,
        )
    }

    @Provides
    fun provideProductsRepository(
        productsApi: ProductsApi,
        productsDataSource: ProductsDataSource,
        @Application coroutineScope: CoroutineScope,
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            productsApi = productsApi,
            productsDataSource = productsDataSource,
            coroutineScope = coroutineScope,
        )
    }
}
