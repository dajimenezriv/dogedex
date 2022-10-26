package com.dajimenezriv.dogedex.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.databinding.FragmentLoginBinding
import kotlin.ClassCastException

class LoginFragment : Fragment() {
    interface LoginFragmentActions {
        fun onRegisterButtonClick()
        fun onLogInFieldsValidated(email: String, password: String)
    }

    private lateinit var loginFragmentActions: LoginFragmentActions
    private lateinit var binding: FragmentLoginBinding

    // when this fragment joins the activity, the activity sends a context to the fragment
    // so, with that context, we can use the methods of the interface anywhere in the fragment??
    // also, the try catch is that the activity must implement LoginFragmentActions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginFragmentActions = try {
            context as LoginFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement LoginFragmentActions")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.loginRegisterButton.setOnClickListener {
            loginFragmentActions.onRegisterButtonClick()
        }
        binding.loginButton.setOnClickListener {
            validateFields()
        }
        return binding.root
    }

    private fun validateFields() {
        var error = false

        binding.emailInput.error = ""
        binding.passwordInput.error = ""

        val email = binding.emailEdit.text.toString()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = true
            binding.emailInput.error = getString(R.string.email_is_not_valid)
        }

        val password = binding.passwordEdit.text.toString()
        if (password.isEmpty()) {
            error = true
            binding.passwordInput.error = getString(R.string.password_is_not_valid)
        }

        if (error) return

        // after we validate the fields, we are going to execute the method onLoginFieldsValidated
        // created on the LoginActivity (must be created because the interface)
        loginFragmentActions.onLogInFieldsValidated(email, password)
    }
}