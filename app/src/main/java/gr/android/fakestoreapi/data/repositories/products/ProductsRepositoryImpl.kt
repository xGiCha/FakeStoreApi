package gr.android.fakestoreapi.data.repositories.products

import gr.android.fakestoreapi.data.local.database.products.ProductsDataSource
import gr.android.fakestoreapi.data.local.database.products.toDomain
import gr.android.fakestoreapi.data.network.models.products.toEntity
import gr.android.fakestoreapi.data.network.services.ProductsApi
import gr.android.fakestoreapi.domain.repository.ProductsRepository
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.utils.Outcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductsRepositoryImpl(
    private val productsApi: ProductsApi,
    private val productsDataSource: ProductsDataSource,
    private val coroutineScope: CoroutineScope,
): ProductsRepository {
    private val errors: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _products =
        productsDataSource.getAllProducts().onStart {
            getFromServer()
        }

    override val products: Flow<Outcome<List<ProductDomainModel>>>
        get() = combine(_products, errors) { result, e ->
            if (result.isEmpty()) {
                Outcome.Error(e.orEmpty())
            } else {
                Outcome.Success(result.map { it.toDomain() })
            }
        }

    private fun getFromServer() {
        coroutineScope.launch {
            try {
                val data = productsApi.getProducts()
                data?.map {
                    productsDataSource.insertProduct(it.toEntity())
                }
            } catch (e: IOException) {
                errors.emit( "Could not reach the server, please check your internet connection!")
            } catch (e: HttpException) {
                errors.emit("Oops, something went wrong!")
            }

        }
    }
}