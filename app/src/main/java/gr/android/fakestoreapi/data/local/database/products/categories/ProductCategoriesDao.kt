package gr.android.fakestoreapi.data.local.database.products.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoriesDao {

    @Query("DELETE FROM products_category_table")
    suspend fun clearTable()

    @Query("SELECT * FROM products_category_table")
    fun getAllProductCategories(): Flow<List<ProductCategoriesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductCategories(productCategoriesEntity: ProductCategoriesEntity)
}