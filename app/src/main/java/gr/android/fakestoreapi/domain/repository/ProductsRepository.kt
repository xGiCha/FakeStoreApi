package gr.android.fakestoreapi.domain.repository

import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    val products: Flow<Outcome<List<ProductDomainModel>>>
}