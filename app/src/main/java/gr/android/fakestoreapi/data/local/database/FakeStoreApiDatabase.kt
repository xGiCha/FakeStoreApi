package gr.android.fakestoreapi.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import gr.android.fakestoreapi.data.local.database.products.ProductEntity
import gr.android.fakestoreapi.data.local.database.products.ProductsDao
import gr.android.fakestoreapi.data.local.database.products.categories.ProductCategoriesDao
import gr.android.fakestoreapi.data.local.database.products.categories.ProductCategoriesEntity

@Database(
    entities = [
        ProductEntity::class,
        ProductCategoriesEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class FakeStoreApiDatabase : RoomDatabase() {
    abstract val dao: ProductsDao
    abstract val productCategoriesDao: ProductCategoriesDao
}