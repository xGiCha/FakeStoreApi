package gr.android.fakestoreapi.data.network.services

import gr.android.fakestoreapi.data.network.models.allUsers.UserDto
import gr.android.fakestoreapi.data.network.models.login.LoginDTO
import gr.android.fakestoreapi.data.network.models.login.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginDTO

    @GET("users/")
    suspend fun getAllUsers(): List<UserDto>
}