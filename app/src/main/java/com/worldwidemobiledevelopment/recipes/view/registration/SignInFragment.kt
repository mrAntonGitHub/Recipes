package com.worldwidemobiledevelopment.recipes.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.email
import kotlinx.android.synthetic.main.fragment_sign_in.message
import kotlinx.android.synthetic.main.fragment_sign_in.password
import kotlinx.android.synthetic.main.fragment_sign_in.signUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInFragment(private val signInAction: SignInAction) : Fragment() {

    interface SignInAction {
        fun onSignUp()
        fun onSignIn(email: String, password: String)
        fun onSignUpWithGoogle()
        fun onSignUpWithFacebook()
        fun onSkipRegistration()
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        signInAsGuest.setOnClickListener {
            signInAction.onSkipRegistration()
        }

        skipRegistration.setOnClickListener{
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
                    message.text = "Введите почту"
                    message.visibility = View.VISIBLE
                }
                password.text?.toString()?.trim().isNullOrEmpty() -> {
                    message.text = "Введите пароль"
                    message.visibility = View.VISIBLE
                }
                else -> {
                    signIn.visibility = View.GONE
                    signInLoading.visibility = View.VISIBLE
                    CoroutineScope(Dispatchers.IO).launch {
                        signInAction.onSignIn(email.text.toString(), password.text.toString())
                    }
                }
            }
        }

    }

    fun showError(string: String) {
        signInLoading.visibility = View.GONE
        signIn.visibility = View.VISIBLE
        signIn.isEnabled = true
        email.isEnabled = true
        password.isEnabled = true
        message.text = string
        message.visibility = View.VISIBLE
    }


}