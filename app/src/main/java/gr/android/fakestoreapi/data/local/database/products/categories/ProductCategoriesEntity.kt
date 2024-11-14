package gr.android.fakestoreapi.data.local.database.products.categories

import androidx.room.Entity
import androidx.room.PrimaryKey
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.utils.Constants.PRODUCTS_TABLE
import gr.android.fakestoreapi.utils.Constants.PRODUCT_CATEGORY_TABLE
import java.security.ProtectionDomain

@Entity(tableName = PRODUCT_CATEGORY_TABLE)
data class ProductCategoriesEntity(
    @PrimaryKey(autoGenerate = false)
    val category: String,
)

fun ProductCategoriesEntity.toDomain(): String {
    return this.category
}

fun String.toEntity(): ProductCategoriesEntity {
    return ProductCategoriesEntity(category = this)
}
