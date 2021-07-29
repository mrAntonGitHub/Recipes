package com.worldwidemobiledevelopment.recipes.view.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.worldwidemobiledevelopment.recipes.R
import kotlinx.android.synthetic.main.fragment_verify.*
import kotlinx.android.synthetic.main.fragment_verify.message
import kotlinx.android.synthetic.main.fragment_verify.registrationLoading

class VerifyFragment(private val verifyAction: VerifyAction) : Fragment() {

    interface VerifyAction{
        fun resendEmailLink()
        fun signIn()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupActionBar()

        resendLink.setOnClickListener {
            resendEmailLink()
        }

        signIn.setOnClickListener {
            signIn()
        }

    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun resendEmailLink() {
        verifyAction.resendEmailLink()
        resendLinkTitle.visibility = View.VISIBLE
    }

    private fun signIn() {
        verifyAction.signIn()
    }

    fun setTime(time: Int){
        if (time != 0){
            resendLink.text = requireActivity().resources.getQuantityString(R.plurals.seconds_left, time, time)
        }else{
            resendLinkTitle.visibility = View.GONE
            resendLink.text = resources.getText(R.string.resend_again)
        }
    }

    fun showException(string: String) {
        registrationLoading.visibility = View.GONE
        signIn.visibility = View.VISIBLE
        signIn.isEnabled = true
        message.text = string
        message.visibility = View.VISIBLE
    }

}