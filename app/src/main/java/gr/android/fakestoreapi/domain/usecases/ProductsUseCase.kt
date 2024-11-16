package gr.android.fakestoreapi.domain.usecases

import gr.android.fakestoreapi.domain.repository.ProductsRepository
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    operator fun invoke(): Flow<Outcome<List<ProductDomainModel>?>> {
        return productsRepository.products
    }

    fun updateProduct(
        id: Int,
        title: String,
        price: Double,
        description: String,
        image: String,
        category: String
    ): Flow<Outcome<Boolean>> {
        return flow {
            emit(
                productsRepository.updateProduct(
                    id = id,
                    title = title,
                    price = price,
                    description = description,
                    image = image,
                    category = category
                )
            )
        }
    }
}