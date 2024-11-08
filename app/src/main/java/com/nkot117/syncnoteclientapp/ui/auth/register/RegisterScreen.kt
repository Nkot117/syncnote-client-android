package com.nkot117.syncnoteclientapp.ui.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.auth.register.model.RegisterFormData
import com.nkot117.syncnoteclientapp.ui.components.CustomLoadingScreen
import com.nkot117.syncnoteclientapp.ui.components.CustomOneButtonDialog
import com.nkot117.syncnoteclientapp.ui.components.CustomOutlinedPasswordTextField
import com.nkot117.syncnoteclientapp.ui.components.CustomOutlinedTextField
import com.nkot117.syncnoteclientapp.util.LogUtil

@Composable
fun RegisterScreen(
    moveLoginScreen: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    LogUtil.d("RegisterScreen")
    val registerUiState by viewModel.uiState.collectAsState()
    val registerFormData by viewModel.registerFormData.collectAsState()

    when (registerUiState) {
        is RegisterUiState.Ideal -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegisterFormContent(
                    viewModel = viewModel,
                    registerFormData = registerFormData,
                    moveLoginScreen = moveLoginScreen
                )
            }
        }

        is RegisterUiState.Error -> {
            val errorMessage = (registerUiState as RegisterUiState.Error).message
            CustomOneButtonDialog(
                message = errorMessage,
                button = "OK",
                onDismiss = {
                    viewModel.clearErrorState()
                }
            )
        }

        is RegisterUiState.Loading -> {
            CustomLoadingScreen()
        }

        is RegisterUiState.Success -> {
            RegisterSuccessContent(
                moveLoginScreen = moveLoginScreen
            )
        }
    }
}

@Composable
fun RegisterFormContent(
    viewModel: RegisterViewModel,
    registerFormData: RegisterFormData,
    moveLoginScreen: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Text(
        text = "サインアップ",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(14.dp))

    CustomOutlinedTextField(
        value = registerFormData.name,
        label = "名前",
        placeholder = "user name",
        isError = registerFormData.errorMessage.containsKey("name"),
        errorMessage = registerFormData.errorMessage["name"],
        onValueChange = {
            viewModel.onNameChanged(it)
        },
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        onImeAction = {
            focusManager.moveFocus(FocusDirection.Next)
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    CustomOutlinedTextField(
        value = registerFormData.email,
        label = "メールアドレス",
        placeholder = "your@email.com",
        isError = registerFormData.errorMessage.containsKey("email"),
        errorMessage = registerFormData.errorMessage["email"],
        onValueChange = {
            viewModel.onEmailChanged(it)
        },
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        onImeAction = {
            focusManager.moveFocus(FocusDirection.Next)
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    CustomOutlinedPasswordTextField(
        value = registerFormData.password,
        label = "パスワード",
        placeholder = "*****",
        isError = registerFormData.errorMessage.containsKey("password"),
        errorMessage = registerFormData.errorMessage["password"],
        onValueChange = {
            viewModel.onPasswordChanged(it)
        },
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next,
        onImeAction = {
            focusManager.moveFocus(FocusDirection.Next)
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    CustomOutlinedPasswordTextField(
        value = registerFormData.confirmPassword,
        label = "確認パスワード",
        placeholder = "*****",
        isError = registerFormData.errorMessage.containsKey("confirmPassword"),
        errorMessage = registerFormData.errorMessage["confirmPassword"],
        onValueChange = {
            viewModel.onConfirmPasswordChanged(it)
        },
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        onImeAction = {
            focusManager.clearFocus()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            viewModel.onSignupClicked()
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text("サインアップ")
    }

    Spacer(modifier = Modifier.height(24.dp))

    ClickableText(
        text = AnnotatedString("ログインはこちら"),
        style = TextStyle(
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        ),
        onClick = {
            moveLoginScreen()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterSuccessContent(
    modifier: Modifier = Modifier,
    moveLoginScreen: () -> Unit = { }
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Column {
            Text(
                "ご登録いただいたメールアドレスに確認メールを送信しました。メール内のリンクをクリックして、登録を完了してください。\nメールが届かない場合は、迷惑メールフォルダをご確認いただくか、再送信をお試しください。",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    moveLoginScreen()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text("ログイン画面へ")
            }
        }
    }
}
