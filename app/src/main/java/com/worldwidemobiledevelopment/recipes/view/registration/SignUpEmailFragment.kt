package com.worldwidemobiledevelopment.recipes.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.email
import kotlinx.android.synthetic.main.fragment_sign_up.message
import kotlinx.android.synthetic.main.fragment_sign_up.password

class SignUpEmailFragment(private val signUpAction: SignUpAction) : Fragment() {

    interface SignUpAction {
        fun signUp(email: String, password: String)
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        setupActionBar()

        signUp.setOnClickListener {
            onRegisterButtonClicked()
        }

    }


    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onRegisterButtonClicked() {
        message.visibility = View.GONE
        signUp.isEnabled = false
        when {
            email.text?.toString()?.trim().isNullOrEmpty() -> {
                message.text = "Введите почту"
                message.visibility = View.VISIBLE
                signUp.isEnabled = true
            }
            password.text?.toString()?.trim().isNullOrEmpty() -> {
                message.text = "Введите пароль"
                message.visibility = View.VISIBLE
                signUp.isEnabled = true
            }
            !agreeToProcessingPD.isChecked -> {
                message.text = "Подтвердите согласие на обработку персональных данных"
                message.visibility = View.VISIBLE
                signUp.isEnabled = true
            }
            else -> {
                email.isEnabled = false
                password.isEnabled = false
                agreeToProcessingPD.isEnabled = false
                signUp.visibility = View.GONE
                registrationLoading.visibility = View.VISIBLE
                signUpAction.signUp(email.text?.trim().toString(), password.text.toString())
            }
        }
    }

    fun showError(string: String) {
        registrationLoading.visibility = View.GONE
        signUp.visibility = View.VISIBLE
        signUp.isEnabled = true
        email.isEnabled = true
        password.isEnabled = true
        agreeToProcessingPD.isEnabled = true
        message.text = string
        message.visibility = View.VISIBLE
    }

}