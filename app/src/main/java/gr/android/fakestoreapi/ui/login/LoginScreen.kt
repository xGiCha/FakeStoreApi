package gr.android.fakestoreapi.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.android.fakestoreapi.R
import gr.android.fakestoreapi.ui.composables.ButtonModal

sealed interface LoginNavigation {
    data object NavigateToHome: LoginNavigation
}

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigate: (LoginNavigation) -> Unit
) {

    LaunchedEffect(loginViewModel.events) {
        loginViewModel.events.collect { event ->
            when(event){
                LoginContract.Event.NavigateToHomeScreen -> {
                    navigate(LoginNavigation.NavigateToHome)
                }
            }
        }
    }

    when(val state = loginViewModel.uiState.collectAsStateWithLifecycle().value) {
        is LoginContract.State.Data -> {
            LoginScreenContent(
                screenInfo = state.screenInfo,
                onLoginClicked = { username, password ->
                    loginViewModel.login(userName = username, password = password)
                },
            )
        }
        else -> {}
    }
}

@Composable
fun LoginScreenContent(
    screenInfo: LoginContract.State.Data.ScreenInfo,
    onLoginClicked: (username: String, password: String) -> Unit,
) {
    var username by remember { mutableStateOf("johnd") }
    var password by remember { mutableStateOf("m38rmF$") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(screenInfo.loginTitle),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (screenInfo.errorMessage.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Red)
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = screenInfo.errorMessage,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            value = username,
            onValueChange = { username = it },
            label = {
                Text(
                    text = stringResource(screenInfo.userNameField)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = screenInfo.userIcon),
                    contentDescription = "Email Icon"
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = stringResource(screenInfo.passwordField)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = screenInfo.lockIcon),
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                val icon = if (passwordVisible) screenInfo.visibilityIcon else screenInfo.visibilityOffIcon
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFFA8A8A9),
                unfocusedTextColor = Color(0xFFA8A8A9)
            )
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(76.dp))

        ButtonModal(
            text = stringResource(screenInfo.buttonText),
            onClick = {
                onLoginClicked(username, password)
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreenContent(
        screenInfo = LoginContract.State.Data.ScreenInfo(
            loginTitle = R.string.login_title,
            buttonText = R.string.login_btn_text,
            passwordField = R.string.password_field,
            userNameField = R.string.user_name_field,
            userIcon = R.drawable.ic_user,
            lockIcon = R.drawable.ic_lock,
            visibilityIcon = Icons.Default.Visibility,
            visibilityOffIcon = Icons.Default.VisibilityOff,
            errorMessage = "Something went wrong"
        ),
        onLoginClicked = { _, _ ->},
    )
}