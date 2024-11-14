package gr.android.fakestoreapi.data.network.services

import gr.android.fakestoreapi.data.network.models.allUsers.UserDto
import gr.android.fakestoreapi.data.network.models.login.LoginDTO
import gr.android.fakestoreapi.data.network.models.login.LoginRequest
import gr.android.fakestoreapi.data.network.models.products.ProductDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDTO>?

    @GET("products/categories")
    suspend fun getProductCategories(): List<String>
}