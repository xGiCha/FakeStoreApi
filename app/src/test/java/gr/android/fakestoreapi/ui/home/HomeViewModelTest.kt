package gr.android.fakestoreapi.ui.home

import app.cash.turbine.test
import gr.android.fakestoreapi.BaseTest
import gr.android.fakestoreapi.CoroutineTestRule
import gr.android.fakestoreapi.domain.uiModels.ProductDomainModel
import gr.android.fakestoreapi.domain.usecases.CategoriesUseCase
import gr.android.fakestoreapi.domain.usecases.ProductsUseCase
import gr.android.fakestoreapi.utils.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class HomeViewModelTest: BaseTest() {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var categoriesUseCase: CategoriesUseCase

    @MockK
    lateinit var productsUseCase: ProductsUseCase

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        homeViewModel = HomeViewModel(
            categoriesUseCase = categoriesUseCase,
            productsUseCase = productsUseCase
        )

        coEvery { productsUseCase.invoke() } returns flowOf (Outcome.Success(listOf()))
        coEvery { categoriesUseCase.invoke() } returns flowOf (Outcome.Success(listOf()))
    }

    @Test
    fun `GIVEN valid products WHEN ALl category is selected THEN get products`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val giveProductListMapped = listOf(dummyHomeProduct).groupBy { it.category }

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        homeViewModel.getProducts()
        homeViewModel.getProductCategories()
        homeViewModel.setSearchText("")
        homeViewModel.selectedCategory("All")

        homeViewModel.uiModels.test {
            assertEquals(
                expected = giveProductListMapped,
                actual = (awaitItem() as? HomeContract.State.Data)?.products

            )
        }
    }

    @Test
    fun `GIVEN valid products WHEN electronics category is selected THEN get electronic products`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val giveProductListMapped = listOf(dummyHomeProduct).groupBy { it.category }

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        homeViewModel.getProducts()
        homeViewModel.getProductCategories()
        homeViewModel.setSearchText("")
        homeViewModel.selectedCategory("electronics")

        homeViewModel.uiModels.test {
            assertEquals(
                expected = giveProductListMapped,
                actual = (awaitItem() as? HomeContract.State.Data)?.products

            )
        }
    }

    @Test
    fun `GIVEN valid products WHEN search term THEN get searched products`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val giveProductListMapped = listOf(dummyHomeProduct).groupBy { it.category }

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        homeViewModel.getProducts()
        homeViewModel.getProductCategories()
        homeViewModel.setSearchText("Woman")
        homeViewModel.selectedCategory("All")

        homeViewModel.uiModels.test {
            assertEquals(
                expected = giveProductListMapped,
                actual = (awaitItem() as? HomeContract.State.Data)?.products

            )
        }
    }

    @Test
    fun `GIVEN valid categories WHEN selectedCategory is enabled THEN get products with selected categories`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val giveProductImages = giveProductList.map { it.id to it.image.orEmpty() }

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))

        homeViewModel.getProducts()
        homeViewModel.getProductCategories()
        homeViewModel.setSearchText("")
        homeViewModel.selectedCategory("All")

        homeViewModel.uiModels.test {
            assertEquals(
                expected = giveProductImages,
                actual = (awaitItem() as? HomeContract.State.Data)?.carouselItems

            )
        }
    }

    @Test
    fun `GIVEN valid categories WHEN search term THEN get searched products`() = runTest {
        val giveProductList = listOf(dummyProductDomainModel)
        val giveCategoriesList = listOf("electronics", "clothes")

        coEvery { productsUseCase.invoke() } returns flowOf(Outcome.Success(giveProductList))
        coEvery { categoriesUseCase.invoke() } returns flowOf(Outcome.Success(giveCategoriesList))

        homeViewModel.getProducts()
        homeViewModel.getProductCategories()
        homeViewModel.setSearchText("")
        homeViewModel.selectedCategory("electronics")

        homeViewModel.uiModels.test {
            assertEquals(
                expected = "electronics",
                actual = (awaitItem() as? HomeContract.State.Data)?.selectedCategory

            )
        }
    }

    val dummyProductDomainModel =
        ProductDomainModel(
            category = "electronics",
            id = 5,
            title = "Woman Printed Kurta",
            image = "",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            price = 1500.0
        )

    val dummyHomeProduct = HomeContract.State.Data.Product(
        category = "electronics",
        id = 5,
        title = "Woman Printed Kurta",
        image = "",
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = "1500.0 €"
    )
}