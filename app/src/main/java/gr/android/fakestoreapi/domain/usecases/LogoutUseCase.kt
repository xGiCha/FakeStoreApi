package gr.android.fakestoreapi.domain.usecases

import gr.android.fakestoreapi.common.annotation.Application
import gr.android.fakestoreapi.data.local.database.products.ProductsDataSource
import gr.android.fakestoreapi.domain.repository.LogoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val logoutRepository: LogoutRepository,
    @Application private val coroutineScope: CoroutineScope,
    private val productsDataSource: ProductsDataSource
) {

    fun logout() {
        coroutineScope.launch {
            logoutRepository.logout()
            productsDataSource.clearTable()
        }
    }
}