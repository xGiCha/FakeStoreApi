package gr.android.fakestoreapi.data.repositories.products

import gr.android.fakestoreapi.data.local.database.products.categories.ProductCategoriesDao
import gr.android.fakestoreapi.data.local.database.products.categories.toDomain
import gr.android.fakestoreapi.data.local.database.products.categories.toEntity
import gr.android.fakestoreapi.data.network.services.ProductsApi
import gr.android.fakestoreapi.domain.repository.ProductCategoriesRepository
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductCategoriesRepositoryImpl(
    private val productsApi: ProductsApi,
    private val productCategoriesDao: ProductCategoriesDao,
    private val coroutineScope: CoroutineScope,
): ProductCategoriesRepository {
    private val errors: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _productsCategories =
        productCategoriesDao.getAllProductCategories().onStart {
            getCategoriesFromServer()
        }

    override val productCategories: Flow<Outcome<List<String>?>>
        get() = combine(_productsCategories, errors) { result, e ->
            if (result.isEmpty()) {
                Outcome.Error(e.orEmpty())
            } else {
                Outcome.Success(listOf("All") + result.map { it.toDomain() })
            }
        }

    private fun getCategoriesFromServer() {
        coroutineScope.launch {
            try {
                val data = productsApi.getProductCategories()
                data?.map {
                    productCategoriesDao.insertProductCategories(it.toEntity())
                }
            } catch (e: IOException) {
                errors.emit( "Could not reach the server, please check your internet connection!")
            } catch (e: HttpException) {
                errors.emit("Oops, something went wrong!")
            }

        }
    }
}