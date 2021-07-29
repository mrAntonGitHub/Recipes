package com.worldwidemobiledevelopment.recipes.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionConfirmPersonalDataProcessing
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionEmailExpected
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionPasswordExpected
import com.worldwidemobiledevelopment.recipes.utils.DynamicTextWatcher
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.email
import kotlinx.android.synthetic.main.fragment_sign_up.emailLayout
import kotlinx.android.synthetic.main.fragment_sign_up.message
import kotlinx.android.synthetic.main.fragment_sign_up.passwordLayout
import kotlinx.android.synthetic.main.fragment_sign_up.signUp

class SignUpEmailFragment(private val signUpAction: SignUpAction) : Fragment() {

    interface SignUpAction {
        fun onSignUp(email: String, password: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
        when {
            email.text?.toString()?.trim().isNullOrEmpty() -> {
                emailLayout.error = ExceptionEmailExpected
            }
            password.text?.toString()?.trim().isNullOrEmpty() -> {
                passwordLayout.error = ExceptionPasswordExpected
            }
            !agreeToProcessingPD.isChecked -> {
                message.text = ExceptionConfirmPersonalDataProcessing
                message.visibility = View.VISIBLE
            }
            else -> {
                email.isEnabled = false
                password.isEnabled = false
                agreeToProcessingPD.isEnabled = false
                signUp.visibility = View.GONE
                registrationLoading.visibility = View.VISIBLE
                signUpAction.onSignUp(email.text?.trim().toString(), password.text?.trim().toString())
            }
        }

        listenEditTextsChanges()
        listenForImeClicked()
    }

    private fun listenEditTextsChanges(){
        // Listen EditText and hide error message
        email.addTextChangedListener(DynamicTextWatcher(
            onChanged = {_, _, _, _ ->
                emailLayout.error = null
            }
        ))

        password.addTextChangedListener(DynamicTextWatcher(
            onChanged = {_, _, _, _ ->
                passwordLayout.error = null
            }
        ))
    }
    private fun listenForImeClicked(){
        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onRegisterButtonClicked()
                true
            } else {
                false
            }
        }
    }

    fun showException(string: String) {
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