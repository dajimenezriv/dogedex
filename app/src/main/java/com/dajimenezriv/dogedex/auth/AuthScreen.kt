package com.dajimenezriv.dogedex.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.auth.AuthNavDestinations.LoginScreenDestination
import com.dajimenezriv.dogedex.auth.AuthNavDestinations.SignUpScreenDestination
import com.dajimenezriv.dogedex.composables.ErrorDialog
import com.dajimenezriv.dogedex.composables.LoadingWheel


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (String, String, String) -> Unit,
    onDialogDismiss: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreenDestination) {
        composable(LoginScreenDestination) {
            LoginScreen(
                viewModel = viewModel,
                onLogInButtonClick = onLoginButtonClick,
                onSignUpButtonClick = { navController.navigate(SignUpScreenDestination) })
        }
        composable(SignUpScreenDestination) {
            SignUpScreen(
                viewModel = viewModel,
                onNavigationIconClick = { navController.navigateUp() },
                onSignUpButtonClick = onSignUpButtonClick
            )
        }
    }

    val status = viewModel.status.value;

    if (status is APIResponseStatus.Loading) LoadingWheel()
    else if (status is APIResponseStatus.Error) {
        ErrorDialog(
            messageId = status.messageId,
            onDialogDismiss = onDialogDismiss
        )
    }
}
