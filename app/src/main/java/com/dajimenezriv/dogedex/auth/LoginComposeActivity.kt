package com.dajimenezriv.dogedex.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dajimenezriv.dogedex.auth.ui.theme.DogedexTheme
import com.dajimenezriv.dogedex.main.MainActivity
import com.dajimenezriv.dogedex.models.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // we only refresh the parts that are inside setContent
        setContent {
            DogedexTheme {
                AuthScreen(
                    onUserLoggedIn = ::startMainActivity,
                )
            }
        }
    }

    private fun startMainActivity(user: User) {
        User.setLoggedInUser(this, user);
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
