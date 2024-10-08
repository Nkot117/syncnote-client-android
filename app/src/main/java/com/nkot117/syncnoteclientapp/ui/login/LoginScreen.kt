package com.nkot117.syncnoteclientapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Preview(showBackground = true)
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
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
            is LoginUiState.Ideal,
            is LoginUiState.Error -> {
                val focusManager = LocalFocusManager.current
                Text(
                    text = "ログイン",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = inputFormData.email,
                    label = { Text("メールアドレス") },
                    placeholder = {
                        Text("your@email.com")
                    },
                    singleLine = true,
                    onValueChange = {
                        viewModel.onEmailChanged(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus((FocusDirection.Next))
                        }
                    ),
                    isError = inputFormData.errorMessage.containsKey("email"),
                    modifier = Modifier.fillMaxWidth()
                )

                if (inputFormData.errorMessage.containsKey("email")) {
                    Text(
                        text = inputFormData.errorMessage["email"] ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inputFormData.password,
                    label = { Text("パスワード") },
                    placeholder = {
                        Text("*****")
                    },
                    singleLine = true,
                    onValueChange = {
                        viewModel.onPasswordChanged(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = inputFormData.errorMessage.containsKey("password"),
                    modifier = Modifier.fillMaxWidth(),
                )

                if (inputFormData.errorMessage.containsKey("password")) {
                    Text(
                        text = inputFormData.errorMessage["password"] ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (false) {
                    CircularProgressIndicator()
                } else {
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
                }

                if (false) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(24.dp))

                ClickableText(
                    text = AnnotatedString("新規登録はこちら"),
                    style = TextStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    ),
                    onClick = { }
                )
            }

            is LoginUiState.Loading -> {
                CircularProgressIndicator()
            }

            is LoginUiState.Success -> {
                Text("ログイン成功")
            }
        }
    }


}
