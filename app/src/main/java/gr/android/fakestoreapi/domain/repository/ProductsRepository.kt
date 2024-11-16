package gr.android.fakestoreapi.domain.repository

import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    val products: Flow<Outcome<List<ProductDomainModel>>>

    suspend fun updateProduct(
        id: Int,
        title: String,
        price: Double,
        description: String,
        image: String,
        category: String
    ): Outcome<Boolean>
}