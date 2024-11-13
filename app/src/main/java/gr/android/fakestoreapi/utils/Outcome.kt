package gr.android.fakestoreapi.utils

sealed class Outcome<out T> {
    data class Success<out T>(val data: T) : Outcome<T>()
    data class Loading<out T>(val data: T? = null) : Outcome<T>()
    data class Error<out T>(val message: String, val data: T? = null) : Outcome<T>()
}