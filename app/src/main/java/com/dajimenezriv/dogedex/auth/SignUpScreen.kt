package com.dajimenezriv.dogedex.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.composables.AuthField
import com.dajimenezriv.dogedex.composables.BackNavigationIcon

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigationIconClick: () -> Unit,
    onSignUpButtonClick: (String, String, String) -> Unit
) {
    val email = remember { mutableStateOf("testing@gmail.com") }
    val password = remember { mutableStateOf("testing") }
    val confirmPassword = remember { mutableStateOf("testing") }

    Scaffold(
        topBar = { SignUpTopBar(onClick = onNavigationIconClick) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthField(
                    labelId = R.string.email,
                    value = email.value,
                    onTextChanged = { email.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    errorMessageId = viewModel.emailError.value
                )

                AuthField(
                    labelId = R.string.password,
                    value = password.value,
                    onTextChanged = { password.value = it },
                    visualTransformation = PasswordVisualTransformation(),
                    errorMessageId = viewModel.passwordError.value
                )

                AuthField(
                    labelId = R.string.confirm_password,
                    value = confirmPassword.value,
                    onTextChanged = { confirmPassword.value = it },
                    visualTransformation = PasswordVisualTransformation(),
                    errorMessageId = viewModel.confirmPasswordError.value
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .semantics { testTag = "signUpButton" },
                    onClick = {
                        onSignUpButtonClick(
                            email.value,
                            password.value,
                            confirmPassword.value
                        )
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.sign_up),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpTopBar(onClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        backgroundColor = Color.Red,
        contentColor = Color.White,
        navigationIcon = { BackNavigationIcon(onClick = onClick) }
    )
}
