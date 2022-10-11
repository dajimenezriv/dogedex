package com.dajimenezriv.dogedex.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dajimenezriv.dogedex.databinding.FragmentLoginBinding
import kotlin.ClassCastException

class LoginFragment : Fragment() {
    interface LoginFragmentActions {
        fun onRegisterButtonClick()
    }

    private lateinit var loginFragmentActions: LoginFragmentActions

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
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)
        binding.loginRegisterButton.setOnClickListener {
            loginFragmentActions.onRegisterButtonClick()
        }
        return binding.root
    }
}