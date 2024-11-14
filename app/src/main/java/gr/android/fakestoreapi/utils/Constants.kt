package gr.android.fakestoreapi.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val BASE_URL = "https://fakestoreapi.com/"
    val AUTH_KEY = stringPreferencesKey(name = "auth_key")
    const val PRODUCTS_TABLE = "products_table"
    const val PRODUCT_CATEGORY_TABLE = "products_category_table"
}