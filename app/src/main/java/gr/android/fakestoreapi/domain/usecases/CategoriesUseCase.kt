package gr.android.fakestoreapi.domain.usecases

import gr.android.fakestoreapi.domain.repository.ProductCategoriesRepository
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CategoriesUseCase @Inject constructor(
    private val productCategoriesRepository: ProductCategoriesRepository
) {

    operator fun invoke(): Flow<Outcome<List<String>?>> {
        return productCategoriesRepository.productCategories
    }
}