package gr.android.fakestoreapi.data.network.services

import gr.android.fakestoreapi.data.network.models.allUsers.UserDto
import gr.android.fakestoreapi.data.network.models.login.LoginDTO
import gr.android.fakestoreapi.data.network.models.login.LoginRequest
import gr.android.fakestoreapi.data.network.models.products.ProductDTO
import gr.android.fakestoreapi.data.network.models.products.ProductUpdateRequestDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDTO>?

    @GET("products/categories")
    suspend fun getProductCategories(): List<String>?

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body productUpdateRequestDTO: ProductUpdateRequestDTO
    ): ProductDTO?
}