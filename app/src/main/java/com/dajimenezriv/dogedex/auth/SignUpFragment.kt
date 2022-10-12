package com.dajimenezriv.dogedex.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    interface SignUpFragmentActions {
        fun onSignUpFieldsValidated(email: String, password: String, confirmPassword: String)
    }

    private lateinit var signUpFragmentActions: SignUpFragmentActions

    // when this fragment joins the activity, the activity sends a context to the fragment
    // so, with that context, we can use the methods of the interface anywhere in the fragment??
    // also, the try catch is that the activity must implement LoginFragmentActions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpFragmentActions = try {
            context as SignUpFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SignUpFragmentActions")
        }
    }

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        binding.signUpButton.setOnClickListener {
            validateFields()
        }
        return binding.root
    }

    private fun validateFields() {
        var error = false

        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""

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

        val confirmPassword = binding.confirmPasswordEdit.text.toString()
        if (confirmPassword.isEmpty()) {
            error = true
            binding.confirmPasswordInput.error = getString(R.string.password_is_not_valid)
        }

        if (password != confirmPassword) {
            error = true
            binding.passwordInput.error = getString(R.string.passwords_do_not_match)
        }

        if (error) return

        // after we validate the fields, we are going to execute the method onSignUpFieldsValidated
        // created on the LoginActivity (must be created because the interface)
        signUpFragmentActions.onSignUpFieldsValidated(email, password, confirmPassword)
    }
}