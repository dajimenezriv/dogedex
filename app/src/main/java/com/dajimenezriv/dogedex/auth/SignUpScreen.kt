package com.dajimenezriv.dogedex.auth

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.composables.BackNavigationIcon

@Composable
fun SignUpScreen(
    status: APIResponseStatus<Any>? = null,
    onNavigationIconClick: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

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
                OutlinedTextField(
                    label = { Text(stringResource(id = R.string.email)) },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    value = email.value,
                    onValueChange = { email.value = it }
                )

                OutlinedTextField(
                    label = { Text(stringResource(id = R.string.password)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = password.value,
                    onValueChange = { password.value = it },
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
                    label = { Text(stringResource(id = R.string.confirm_password)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {}
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.register),
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
