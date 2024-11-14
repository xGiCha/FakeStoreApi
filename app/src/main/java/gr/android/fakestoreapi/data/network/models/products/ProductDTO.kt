package gr.android.fakestoreapi.data.network.models.products

import com.google.gson.annotations.SerializedName
import gr.android.fakestoreapi.data.local.database.products.ProductEntity
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel

data class ProductDTO(
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("title")
    val title: String? = null
)

fun ProductDTO.toDomain(): ProductDomainModel {
    return ProductDomainModel(
        category = category,
        description = description,
        id = id,
        image = image,
        price = price,
        title = title
    )
}

fun ProductDTO.toEntity(): ProductEntity {
    return ProductEntity(
        category = category,
        description = description,
        id = id ?: -1,
        image = image,
        price = price,
        title = title
    )
}
