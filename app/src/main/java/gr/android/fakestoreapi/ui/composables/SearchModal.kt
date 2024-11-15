package gr.android.fakestoreapi.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.theme.IconGray
import gr.android.fakestoreapi.ui.theme.LightGray
import gr.android.fakestoreapi.ui.theme.PlaceholderGray

@Composable
fun SearchModal(
    modifier: Modifier = Modifier,
    currentSearchText: String?,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isTextFieldFocused = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), clip = false)
            .clickable {
                // Hide keyboard and clear focus when clicking outside the TextField
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        // Search TextField
        TextField(
            value = currentSearchText.orEmpty(),
            onValueChange = {
                onSearchTextChange(it)
            },
            placeholder = {
                Text(
                    text = "Search any Product...",
                    color = PlaceholderGray,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isTextFieldFocused.value = focusState.isFocused
                },
            shape = RectangleShape,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            maxLines = 1,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_seacrh),
                    contentDescription = null,
                    tint = IconGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}

@Preview
@Composable
private fun SearchModalPreview() {
    SearchModal(
        currentSearchText = "",
        onSearchTextChange = {},
        onSearch = {},
    )
}
