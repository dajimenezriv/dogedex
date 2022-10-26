package com.dajimenezriv.dogedex.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dajimenezriv.dogedex.auth.ui.theme.DogedexTheme
import com.dajimenezriv.dogedex.main.MainActivity
import com.dajimenezriv.dogedex.models.User

class LoginComposeActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = viewModel.user.value

        if (user != null) {
            User.setLoggedInUser(this, user);
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setContent {
            DogedexTheme {
                AuthScreen(
                    viewModel = viewModel,
                    onLoginButtonClick = { email, password -> viewModel.logIn(email, password) },
                    onSignUpButtonClick = { email, password, confirmPassword ->
                        viewModel.signUp(
                            email,
                            password,
                            confirmPassword
                        )
                    },
                    onDialogDismiss = { viewModel.resetAPIResponseStatus() }
                )
            }
        }
    }
}
