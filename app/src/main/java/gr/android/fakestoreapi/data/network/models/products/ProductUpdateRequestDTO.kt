package gr.android.fakestoreapi.data.network.models.products

import com.google.gson.annotations.SerializedName

data class ProductUpdateRequestDTO(
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("title")
    val title: String? = null
)
