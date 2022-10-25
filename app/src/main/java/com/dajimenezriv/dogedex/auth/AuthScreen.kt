package com.dajimenezriv.dogedex.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dajimenezriv.dogedex.auth.AuthNavDestinations.LoginScreenDestination
import com.dajimenezriv.dogedex.auth.AuthNavDestinations.SignUpScreenDestination


@Composable
fun AuthScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreenDestination) {
        composable(LoginScreenDestination) {
            LoginScreen(onRegisterButtonClick = {
                navController.navigate(
                    SignUpScreenDestination
                )
            })
        }
        composable(SignUpScreenDestination) {
            SignUpScreen(onNavigationIconClick = {
                navController.navigateUp()
            })
        }
    }
}
