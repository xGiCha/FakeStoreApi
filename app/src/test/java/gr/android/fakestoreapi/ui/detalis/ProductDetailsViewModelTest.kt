package gr.android.fakestoreapi.ui.detalis

import app.cash.turbine.test
import gr.android.fakestoreapi.BaseTest
import gr.android.fakestoreapi.CoroutineTestRule
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo
import gr.android.fakestoreapi.ui.detalis.ProductDetailsContract.State.Data.ProductDetailsScreenInfo.ToolBarInfo
import gr.android.fakestoreapi.utils.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


class ProductDetailsViewModelTest: BaseTest() {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var productsUseCase: ProductsUseCase

    private lateinit var productDetailsViewModel: ProductDetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        productDetailsViewModel = ProductDetailsViewModel(productsUseCase)

        coEvery { productsUseCase.invoke() } returns flowOf (Outcome.Success(listOf()))
    }

    @Test
    fun `GIVEN valid products WHEN product is selected THEN get product details`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        productDetailsViewModel.setProduct(5)
        productDetailsViewModel.getProducts()

        productDetailsViewModel.uiModels.test {
            assertEquals(
                expected = dummyProductState,
                actual = (awaitItem() as? ProductDetailsContract.State.Data)?.product

            )
        }
    }

    @Test
    fun `GIVEN invalid products WHEN product is selected THEN get error`() = runTest {

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Error("Error"))

        productDetailsViewModel.setProduct(5)
        productDetailsViewModel.getProducts()

        productDetailsViewModel.uiModels.test {
            assertEquals(
                expected = "Error",
                actual = (awaitItem() as? ProductDetailsContract.State.Error)?.value

            )
        }
    }
    @Test
    fun `GIVEN valid products WHEN product is selected THEN product details`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val givenProductDetailsScreenInfo = dummyProductDetailsScreenInfo

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        productDetailsViewModel.setProduct(5)
        productDetailsViewModel.getProducts()

        productDetailsViewModel.uiModels.test {
            assertEquals(
                expected = givenProductDetailsScreenInfo,
                actual = (awaitItem() as? ProductDetailsContract.State.Data)?.productDetailsScreenInfo

            )
        }
    }

    @Test
    fun `GIVEN product details WHEN pressing back THEN navigate to home screen`() = runTest {

        val events = productDetailsViewModel.events.testSubscribe()
        productDetailsViewModel.onBack()
        events.assertLast {
            assertEquals(
                ProductDetailsContract.Event.OnBack,
                it
            )
        }.dispose()
    }

    @Test
    fun `GIVEN product details WHEN pressing edit THEN navigate to edit product screen`() = runTest {

        val events = productDetailsViewModel.events.testSubscribe()
        productDetailsViewModel.navigateToDetailsScreen()
        events.assertLast {
            assertEquals(
                ProductDetailsContract.Event.NavigateToEditProductScreen,
                it
            )
        }.dispose()
    }
}

val dummyProductDomainModel =
    ProductDomainModel(
        category = "All",
        id = 5,
        title = "Woman Printed Kurta",
        image = "",
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = 1500.0
    )

val dummyProductState = ProductDetailsContract.State.Data.Product (
    category = "All",
    id = 5,
    title = "Woman Printed Kurta",
    image = "",
    description = "Neque porro quisquam est qui dolorem ipsum quia",
    price = "1500.0 €"
)

val dummyProductDetailsScreenInfo = ProductDetailsScreenInfo(
    title = R.string.product_details_title,
    toolBarInfo = ToolBarInfo(
        toolbarLeftIcon = R.drawable.ic_left_arrow,
        toolMiddleIcon = R.drawable.ic_toolbar,
        toolRightIcon = R.drawable.ic_edit_product,
        toolLeftIconVisibility = true,
        toolMiddleIconVisibility = false,
        toolRightIconVisibility = true
    )
)