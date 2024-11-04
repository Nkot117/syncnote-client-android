package com.nkot117.syncnoteclientapp.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.auth.login.model.LoginFormData
import com.nkot117.syncnoteclientapp.ui.components.CustomDialog
import com.nkot117.syncnoteclientapp.ui.components.CustomOutlinedPasswordTextField
import com.nkot117.syncnoteclientapp.ui.components.CustomOutlinedTextField
import com.nkot117.syncnoteclientapp.util.LogUtil

@Composable
fun LoginScreen(
    moveRegisterScreen: () -> Unit,
    moveHomeScreen: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    LogUtil.d("LoginScreen")
    val loginUiState by viewModel.uiState.collectAsState()
    val inputFormData by viewModel.loginFormData.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (loginUiState) {
            is LoginUiState.Ideal -> {
                LoginFormContent(
                    moveRegisterScreen = moveRegisterScreen,
                    viewModel = viewModel,
                    inputFormData = inputFormData
                )
            }

            is LoginUiState.Error -> {
                val errorMessage = (loginUiState as LoginUiState.Error).message
                CustomDialog(message = errorMessage, button = "OK", onDismiss = {
                    viewModel.clearErrorState()
                })
            }

            is LoginUiState.Loading -> {
                CircularProgressIndicator()
            }

            is LoginUiState.Success -> {
                LogUtil.d("Login Success")
                LaunchedEffect(Unit) {
                    moveHomeScreen()
                }
            }
        }
    }
}

@Composable
fun LoginFormContent(
    moveRegisterScreen: () -> Unit,
    viewModel: LoginViewModel,
    inputFormData: LoginFormData
) {
    val focusManager = LocalFocusManager.current
    Text(
        text = "ログイン",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(14.dp))

    CustomOutlinedTextField(
        value = inputFormData.email,
        label = "メールアドレス",
        placeholder = "your@email.com",
        isError = inputFormData.errorMessage.containsKey("email"),
        errorMessage = inputFormData.errorMessage["email"],
        onValueChange = {
            viewModel.onEmailChanged(it)
        },
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        onImeAction = {
            focusManager.moveFocus(FocusDirection.Next)
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    CustomOutlinedPasswordTextField(
        value = inputFormData.password,
        label = "パスワード",
        placeholder = "*****",
        isError = inputFormData.errorMessage.containsKey("password"),
        errorMessage = inputFormData.errorMessage["password"],
        onValueChange = {
            viewModel.onPasswordChanged(it)
        },
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        onImeAction = {
            focusManager.clearFocus()
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            viewModel.onLoginClicked()
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text("ログイン")
    }

    Spacer(modifier = Modifier.height(24.dp))

    ClickableText(
        text = AnnotatedString("新規登録はこちら"),
        style = TextStyle(
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        ),
        onClick = {
            moveRegisterScreen()
        }
    )
}



