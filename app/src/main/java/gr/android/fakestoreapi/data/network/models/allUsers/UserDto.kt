package gr.android.fakestoreapi.data.network.models.allUsers

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("address")
    val address: Address,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: Name,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("__v")
    val v: Int
)