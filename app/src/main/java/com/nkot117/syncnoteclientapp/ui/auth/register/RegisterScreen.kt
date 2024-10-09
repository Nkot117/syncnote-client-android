package com.nkot117.syncnoteclientapp.ui.auth.register

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun RegisterScreen(
    moveLoginScreen: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val registerUiState by viewModel.uiState.collectAsState()
    val registerFormData by viewModel.registerFormData.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when(registerUiState) {
            is RegisterUiState.Ideal,
            is RegisterUiState.Error -> {
                val focusManager = LocalFocusManager.current
                Text(
                    text = "サインアップ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = registerFormData.name,
                    label = { Text("名前") },
                    placeholder = {
                        Text("user name")
                    },
                    singleLine = true,
                    onValueChange = {
                        viewModel.onNameChanged(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus((FocusDirection.Next))
                        }
                    ),
                    isError = registerFormData.errorMessage.containsKey("name"),
                    modifier = Modifier.fillMaxWidth()
                )

                if (registerFormData.errorMessage.containsKey("name")) {
                    Text(
                        text = registerFormData.errorMessage["name"] ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = registerFormData.email,
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
                    isError = registerFormData.errorMessage.containsKey("email"),
                    modifier = Modifier.fillMaxWidth()
                )

                if (registerFormData.errorMessage.containsKey("email")) {
                    Text(
                        text = registerFormData.errorMessage["email"] ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = registerFormData.password,
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus((FocusDirection.Next))
                        }
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = registerFormData.errorMessage.containsKey("password"),
                    modifier = Modifier.fillMaxWidth(),
                )

                if (registerFormData.errorMessage.containsKey("password")) {
                    Text(
                        text = registerFormData.errorMessage["password"] ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = registerFormData.confirmPassword,
                    label = { Text("確認パスワード") },
                    placeholder = {
                        Text("*****")
                    },
                    singleLine = true,
                    onValueChange = {
                        viewModel.onConfirmPasswordChanged(it)
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
                    isError = registerFormData.errorMessage.containsKey("confirmPassword"),
                    modifier = Modifier.fillMaxWidth(),
                )

                if (registerFormData.errorMessage.containsKey("confirmPassword")) {
                    Text(
                        text = registerFormData.errorMessage["confirmPassword"] ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

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

                if (false) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("", color = Color.Red)
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
            is RegisterUiState.Loading -> {
                CircularProgressIndicator()
            }

            is RegisterUiState.Success -> {
                Text("サインアップ成功")
            }
        }
    }
}
