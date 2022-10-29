package com.dajimenezriv.dogedex.auth

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.auth.AuthNavDestinations.LoginScreenDestination
import com.dajimenezriv.dogedex.auth.AuthNavDestinations.SignUpScreenDestination
import com.dajimenezriv.dogedex.composables.ErrorDialog
import com.dajimenezriv.dogedex.composables.LoadingWheel
import com.dajimenezriv.dogedex.models.User

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onUserLoggedIn: (User) -> Unit,
) {
    val user = viewModel.user.value
    if (user != null) onUserLoggedIn(user)

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreenDestination) {
        composable(LoginScreenDestination) {
            LoginScreen(
                viewModel = viewModel,
                onLogInButtonClick = { email, password -> viewModel.logIn(email, password) },
                onSignUpButtonClick = { navController.navigate(SignUpScreenDestination) })
        }
        composable(SignUpScreenDestination) {
            SignUpScreen(
                viewModel = viewModel,
                onNavigationIconClick = { navController.navigateUp() },
                onSignUpButtonClick = { email, password, confirmPassword ->
                    viewModel.signUp(
                        email,
                        password,
                        confirmPassword
                    )
                }
            )
        }
    }

    val status = viewModel.status.value;

    if (status is APIResponseStatus.Loading) LoadingWheel()
    else if (status is APIResponseStatus.Error) {
        ErrorDialog(
            messageId = status.messageId,
            onDialogDismiss = { viewModel.resetAPIResponseStatus() }
        )
    }
}
