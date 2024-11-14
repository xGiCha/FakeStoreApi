package gr.android.fakestoreapi.domain.usecases

import gr.android.fakestoreapi.domain.repository.ProductsRepository
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    operator fun invoke(): Flow<Outcome<List<ProductDomainModel>?>> {
        return productsRepository.products
    }
}