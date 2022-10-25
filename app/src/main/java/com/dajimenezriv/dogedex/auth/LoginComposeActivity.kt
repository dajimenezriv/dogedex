package com.dajimenezriv.dogedex.auth

import android.os.Bundle
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

class LoginComposeActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogedexTheme {
                SignUpScreen(onNavigationIconClick = {})
            }
        }
    }
}
