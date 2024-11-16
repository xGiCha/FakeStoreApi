package gr.android.fakestoreapi.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.android.fakestoreapi.ui.composables.ButtonModal
import gr.android.fakestoreapi.ui.composables.CardModal
import gr.android.fakestoreapi.ui.composables.SmallMessageModal
import gr.android.fakestoreapi.ui.composables.TopBarModal

sealed interface UpdateProductScreenNavigation {
    data object OnBack : UpdateProductScreenNavigation
}

@Composable
fun UpdateProductScreen(
    updateProductViewModel: UpdateProductViewModel = hiltViewModel(),
    navigate: (UpdateProductScreenNavigation) -> Unit,
    productId: Int?,
) {
    updateProductViewModel.setProduct(productId)

    LaunchedEffect(updateProductViewModel.events) {
        updateProductViewModel.events.collect {
            when (it) {
                UpdateProductContract.Event.OnBack -> {
                    navigate(UpdateProductScreenNavigation.OnBack)
                }

            }
        }
    }

    when (val state = updateProductViewModel.uiState.collectAsStateWithLifecycle().value) {
        is UpdateProductContract.State.Data -> {
            UpdateProductScreenContent(
                navigate = {
                    when (it) {
                        UpdateProductScreenNavigation.OnBack -> {
                            updateProductViewModel.onBack()
                        }
                    }
                },
                product = state.product,
                onSaveClick = { title, price, category, description ->
                    updateProductViewModel.updateProduct(
                        id = state.product?.id,
                        title = title,
                        price = price,
                        description = description,
                        image = state.product?.image,
                        category = category
                    )
                },
                showErrorMessage = state.showErrorMessage,
                onHideErrorMessage = {
                    updateProductViewModel.hideErrorMessage()
                }
            )
        }

        else -> {}
    }

}

@Composable
fun UpdateProductScreenContent(
    navigate: (UpdateProductScreenNavigation) -> Unit,
    product: UpdateProductContract.State.Data.Product?,
    onSaveClick: (
        title: String,
        price: String,
        category: String,
        description: String
    ) -> Unit,
    showErrorMessage: String,
    onHideErrorMessage: () -> Unit,
) {
    val title = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarModal(
                modifier = Modifier,
                middleIconVisibility = false,
                leftIconVisibility = true,
                rightIconVisibility = false,
                onBackClick = {
                    navigate(UpdateProductScreenNavigation.OnBack)
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                CardModal(
                    image = product?.image.orEmpty()
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

                CustomTextField(
                    label = "Title",
                    value = title.value,
                    onValueChange = { title.value = it }
                )
                CustomTextField(
                    label = "Price",
                    value = price.value,
                    onValueChange = { price.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                CustomTextField(
                    label = "Category",
                    value = category.value,
                    onValueChange = { category.value = it }
                )
                CustomTextField(
                    label = "Description",
                    value = description.value,
                    onValueChange = { description.value = it }
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        ButtonModal(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            text = "Save",
            onClick = {
                onSaveClick(
                    title.value,
                    price.value,
                    category.value,
                    description.value
                )
            }
        )

        if (showErrorMessage.isNotEmpty()) {
            SmallMessageModal(
                errorMessage = showErrorMessage,
                onClick = {
                    onHideErrorMessage()
                }
            )
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified)
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 16.sp),
            keyboardOptions = keyboardOptions
        )
    }
}

@Preview
@Composable
private fun UpdateProductScreenContentPreview() {
    val product = UpdateProductContract.State.Data.Product(
        category = "All",
        id = 5,
        title = "Woman Printed Kurta",
        image = "",
        description = "Neque porro quisquam est qui dolorem ipsum quia",
        price = "1500 €"
    )
    UpdateProductScreenContent(
        navigate = {},
        product = product,
        onSaveClick = { _, _, _, _ -> },
        showErrorMessage = "",
        onHideErrorMessage = {}
    )
}