package com.dajimenezriv.dogedex.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.dajimenezriv.dogedex.main.MainActivity
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.databinding.ActivityLoginBinding
import com.dajimenezriv.dogedex.models.User

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions,
    SignUpFragment.SignUpFragmentActions {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        viewModel.status.observe(this) { status ->
            when (status) {
                is APIResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    showErrorDialog(status.messageId)
                }
                is APIResponseStatus.Loading -> {
                    // show progress bar
                    binding.loadingWheel.visibility = View.VISIBLE
                }
                is APIResponseStatus.Success -> {
                    // hide progress bar
                    binging.loadingWheel.visibility = View.GONE
                }
            }
        }

        // when we start the ViewModel, the user starts being null
        viewModel.user.observe(this) { user ->
            if (user != null) {
                // save session
                User.setLoggedInUser(this, user);
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        */
    }

    private fun showErrorDialog(messageId: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.there_was_an_error)
            .setMessage(messageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /** Dismiss dialog */ }
            .create()
            .show()
    }

    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLogInFieldsValidated(email: String, password: String) {
        viewModel.logIn(email, password);
    }

    override fun onSignUpFieldsValidated(email: String, password: String, confirmPassword: String) {
        // it's going to call the method in our ViewModel (signUp)
        viewModel.signUp(email, password, confirmPassword)
    }
}