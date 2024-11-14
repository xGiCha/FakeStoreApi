package gr.android.fakestoreapi.data.local.database.products

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsDataSource @Inject constructor(
    private val productsDao: ProductsDao
) {

    suspend fun insertProduct(productEntity: ProductEntity) {
        productsDao.insertProduct(productEntity)
    }

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productsDao.getAllProducts()
    }

    suspend fun clearTable() {
        productsDao.clearTable()
    }
}