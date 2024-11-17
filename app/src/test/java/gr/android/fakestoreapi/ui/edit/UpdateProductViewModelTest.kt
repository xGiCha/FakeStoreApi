package gr.android.fakestoreapi.ui.edit

import app.cash.turbine.test
import gr.android.fakestoreapi.BaseTest
import gr.android.fakestoreapi.CoroutineTestRule
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.ui.detalis.dummyProductDomainModel
import gr.android.fakestoreapi.utils.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class UpdateProductViewModelTest: BaseTest() {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var productsUseCase: ProductsUseCase

    private lateinit var updateProductViewModel: UpdateProductViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        updateProductViewModel = UpdateProductViewModel(productsUseCase)
        coEvery { productsUseCase.invoke() } returns flowOf (Outcome.Success(listOf()))
        coEvery { productsUseCase.updateProduct(0, "", image = "", price = 0.0, category = "", description = "" ) } returns flowOf (Outcome.Success(false))
    }

    @Test
    fun `GIVEN valid products WHEN product is selected THEN get product details`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        updateProductViewModel.setProduct(5)
        updateProductViewModel.getProducts()

        updateProductViewModel.uiModels.test {
            assertEquals(
                expected = dummyProductState,
                actual = (awaitItem() as? UpdateProductContract.State.Data)?.product

            )
        }
    }

    @Test
    fun `GIVEN valid products WHEN update product THEN get product's get updated`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))
        coEvery { productsUseCase.updateProduct(
            id = 5,
            title = "Woman Printed Kurta",
            image = "",
            price = 1500.0,
            category = "All",
            description = "Neque porro quisquam est qui dolorem ipsum quia"
        ) } returns flowOf (Outcome.Success(true))

        updateProductViewModel.setProduct(5)
        updateProductViewModel.getProducts()
        updateProductViewModel.updateProduct(
            id = 5,
            title = "Woman Printed Kurta",
            image = "",
            price = "1500.0",
            category = "All",
            description = "Neque porro quisquam est qui dolorem ipsum quia"
        )

        updateProductViewModel.uiModels.test {
            assertEquals(
                expected = dummyProductState,
                actual = (awaitItem() as? UpdateProductContract.State.Data)?.product

            )
        }
    }
}

val dummyProductState = UpdateProductContract.State.Data.Product (
    category = "All",
    id = 5,
    title = "Woman Printed Kurta",
    image = "",
    description = "Neque porro quisquam est qui dolorem ipsum quia",
    price = "1500.0 €"
)