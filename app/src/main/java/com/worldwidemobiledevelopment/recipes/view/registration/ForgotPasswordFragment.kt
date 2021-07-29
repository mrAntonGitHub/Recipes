package com.worldwidemobiledevelopment.recipes.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionEmailExpected
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment(private val forgotPasswordAction: ForgotPasswordAction) : Fragment(),
    InfoDialogFragment.InfoDialogAction {

    interface ForgotPasswordAction {
        fun onSendReset(email: String)
        fun onReturnToMainPage()
    }

    lateinit var infoDialog : InfoDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupActionBar()

        sendEmail.setOnClickListener {
            val emailText = email.text?.trim().toString()
            if (emailText.isNotEmpty()) {
                forgotPasswordAction.onSendReset(emailText)
            } else {
                email.error = ExceptionEmailExpected
            }
        }
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showInfoDialog() {
        infoDialog = InfoDialogFragment(this)
        infoDialog.show(requireActivity().supportFragmentManager, "INFO DIALOG")
    }

    fun showException(string: String) {
        sendEmail.isEnabled = true
        message.text = string
        message.setTextColor(requireActivity().getColor(android.R.color.holo_red_light))
        message.visibility = View.VISIBLE
    }

    override fun onMainPage() {
        infoDialog.dismiss()
        forgotPasswordAction.onReturnToMainPage()
    }

}