package com.worldwidemobiledevelopment.recipes.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionEmailExpected
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionPasswordExpected
import com.worldwidemobiledevelopment.recipes.utils.DynamicTextWatcher
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment(private val signInAction: SignInAction) : Fragment() {

    interface SignInAction {
        fun onSignUp()
        fun onSignUpWithGoogle()
        fun onSignUpWithFacebook()
        fun onSignIn(email: String, password: String)
        fun onForgotPassword()
        fun onSkipRegistration()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        signInAsGuest.setOnClickListener {
            signInAction.onSkipRegistration()
        }

        forgotPassword.setOnClickListener {
            signInAction.onForgotPassword()
        }

        skipRegistration.setOnClickListener {
            signInAction.onSkipRegistration()
        }

        signUp.setOnClickListener {
            signInAction.onSignUp()
        }

        signInWithGoogle.setOnClickListener {
            signInAction.onSignUpWithGoogle()
        }

        signInWithFacebook.setOnClickListener {
            signInAction.onSignUpWithFacebook()
        }

        signIn.setOnClickListener {
            when {
                email.text?.toString()?.trim().isNullOrEmpty() -> {
                    emailLayout.error = ExceptionEmailExpected
                }
                password.text?.toString()?.trim().isNullOrEmpty() -> {
                    passwordLayout.error = ExceptionPasswordExpected
                }
                else -> {
                    signIn()
                }
            }
        }

        email.addTextChangedListener(DynamicTextWatcher(
            onChanged = { _, _, _, _ ->
                emailLayout.error = null
            }
        ))

        password.addTextChangedListener(DynamicTextWatcher(
            onChanged = { _, _, _, _ ->
                passwordLayout.error = null
            }
        ))

        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signIn.callOnClick()
                true
            } else {
                false
            }
        }

    }

    fun showException(string: String) {
        signInLoading.visibility = View.GONE
        signIn.text = resources.getText(R.string.enter)
        signIn.isEnabled = true
        email.isEnabled = true
        password.isEnabled = true
        message.text = string
        message.visibility = View.VISIBLE
    }

    private fun signIn() {
        signIn.text = ""
        signIn.isEnabled = false
        email.isEnabled = false
        password.isEnabled = false
        signInLoading.visibility = View.VISIBLE
        signInAction.onSignIn(email.text.toString(), password.text.toString())
    }
}