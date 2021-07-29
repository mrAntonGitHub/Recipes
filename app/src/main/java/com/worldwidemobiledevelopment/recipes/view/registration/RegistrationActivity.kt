package com.worldwidemobiledevelopment.recipes.view.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.worldwidemobiledevelopment.recipes.MainActivity
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.ResultStatus
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.ExceptionConfirmEmail
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseAuthFailToEnterUsingGoogle
import com.worldwidemobiledevelopment.recipes.utils.Utils.Companion.hideKeyboard
import com.worldwidemobiledevelopment.recipes.view.splash.APP_PREFERENCES_NAME
import com.worldwidemobiledevelopment.recipes.view.splash.IS_FIRST_LAUNCH
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_verify.signIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Duration

const val GOOGLE_SIGN_IN: Int = 600

class RegistrationActivity : AppCompatActivity(), SignInFragment.SignInAction,
    SignUpEmailFragment.SignUpAction, ForgotPasswordFragment.ForgotPasswordAction,
    VerifyFragment.VerifyAction {

    lateinit var viewModel: RegistrationViewModel

    private lateinit var signInFragment: SignInFragment
    private lateinit var signUpEmailFragment: SignUpEmailFragment
    private lateinit var forgotPasswordFragment: ForgotPasswordFragment
    private lateinit var verifyFragment: VerifyFragment

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        launchInitialFragment()
    }

    private fun launchInitialFragment() {
        signInFragment = SignInFragment(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, signInFragment, "SIGN_IN_FRAGMENT").commit()
    }

    // SignInFragment Callbacks
    override fun onSignUp() {
        signUpEmailFragment = SignUpEmailFragment(this)
        changeFragment(signUpEmailFragment, "SIGN_UP_FRAGMENT", "SIGN_IN_FRAGMENT" )
    }
    override fun onSignUpWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }
    override fun onSignUpWithFacebook() {
        Toast.makeText(this, "onSignInWithFacebook", Toast.LENGTH_SHORT).show()
    }
    override fun onSignIn(email: String, password: String) {
        lifecycleScope.launch {
            val result = viewModel.signIn(email, password)
            if (result.status == ResultStatus.SUCCESS) {
                val isVerified = viewModel.isEmailVerified()
                if (isVerified.status == ResultStatus.SUCCESS) {
                    if (isVerified.data == true) {
                        startMainActivity()
                    } else {
                        showVerifyEmailSnackbar()
                    }
                } else {
                    showSnackBar("Произошла ошибка. Попробуйте позже")
                }
            } else {
                Log.e("RegistrationActivity.kt", "Error while sign in : ${result.message} ${result.exception}")
                if (!signInFragment.isDetached) {
                    result.message?.let { signInFragment.showException(it) }
                    showSnackBar("Проверьте подключение к интернету")
                }
            }
        }
    }
    override fun onForgotPassword() {
        forgotPasswordFragment = ForgotPasswordFragment(this)
        changeFragment(forgotPasswordFragment, "FORGOT_PASSWORD", "SIGN_IN_FRAGMENT")
    }
    override fun onSkipRegistration() {
        startMainActivity()
    }

    // SignUpEmailFragment Callbacks
    override fun onSignUp(email: String, password: String) {
        lifecycleScope.launch {
            val result = viewModel.signUp(email, password)
            if (result.status == ResultStatus.SUCCESS) {

                verifyFragment = VerifyFragment(this@RegistrationActivity)
                changeFragment(verifyFragment, "VERIFY_FRAGMENT")
                sendVerificationEmail()

            } else {
                Log.e("RegistrationActivity", "${result.message} : ${result.exception}")
                if (!signUpEmailFragment.isDetached) {
                    result.message?.let { signUpEmailFragment.showException(it) }
                }
            }
        }
    }

    // ForgotPasswordFragment Callbacks
    override fun onSendReset(email: String) {
        lifecycleScope.launch {
            val result = viewModel.resetPassword(email)
            if (result.status == ResultStatus.SUCCESS) {
                forgotPasswordFragment.showInfoDialog()
            } else {
                Log.e("RegistrationActivity", "$result")
                forgotPasswordFragment.showException(result.message.toString())
            }
        }
    }
    override fun onReturnToMainPage() {
        onBackPressed()
    }

    // VerifyFragment Callbacks
    override fun resendEmailLink() {
        if (viewModel.secondsLeft.value == 0) {
            lifecycleScope.launch {
                viewModel.verifyUserEmail()
            }
            sendVerificationEmail()
        }
    }
    override fun signIn() {
        lifecycleScope.launch {
            val result = viewModel.isEmailVerified()

            if (result.status == ResultStatus.SUCCESS) {
                if (result.data == true) {
                    startMainActivity()
                } else {
                    verifyFragment.showException("Почта не подтверждена. Перейдите по ссылке в письме для активации аккаунта")
                    Log.e("RegistrationActivity", "${result.data}")
                }
            } else {
                Log.e(
                    "RegistrationActivity",
                    "Ошибка обращения к пользователю: ${result.exception}"
                )
                verifyFragment.showException("Почта не подтверждена.")
            }

        }
    }

    // Google SignIn Callback
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Get result from google signUp
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                lifecycleScope.launch {
                    val result = viewModel.signInWithGoogle(account.idToken!!)
                    if (result.status == ResultStatus.SUCCESS) {
                        startMainActivity()
                    } else {
                        Log.e("RegistrationActivity", "Error while signing in with Google", result.exception)
                        result.message?.let { signInFragment.showException(it) }
                    }
                }
            } catch (e: ApiException) {
                Log.e("RegistrationActivity", "Google sign in failed", e)
                signInFragment.showException(FirebaseAuthFailToEnterUsingGoogle)
            }
        }
    }


    private fun changeFragment(fragment : Fragment, fragmentTag: String, addToBackStackName: String? = null){
        val transaction = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.animator.slide_left_enter,
                R.animator.slide_left_exit,
                R.animator.slide_right_enter,
                R.animator.slide_right_exit)

        if (addToBackStackName != null){
            transaction.addToBackStack(addToBackStackName)
                .replace(R.id.fragment, fragment, fragmentTag)
        }else{
            transaction.replace(R.id.fragment, fragment, fragmentTag)
        }
        transaction.commit()
    }
    private fun showSnackBar(string: String, duration: Int = Snackbar.LENGTH_LONG) {
        signInFragment.signInLoading.visibility = View.GONE
        signInFragment.signIn.visibility = View.VISIBLE
        this.hideKeyboard()
        signInFragment.view?.let { Snackbar.make(it, string, duration) }?.show()
    }
    private fun showVerifyEmailSnackbar() {
        signInFragment.signInLoading.visibility = View.GONE
        signInFragment.signIn.visibility = View.VISIBLE
        this.hideKeyboard()
        signInFragment.view?.let {
            Snackbar.make(it, ExceptionConfirmEmail, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.confirm)) {
                    verifyFragment = VerifyFragment(this@RegistrationActivity)
                    changeFragment(verifyFragment, "VERIFY_FRAGMENT", "SIGN_IN_FRAGMENT")
                    sendVerificationEmail()
                }.show()
        }
    }
    private fun sendVerificationEmail() {
        lifecycleScope.launch {
            viewModel.sendVerificationEmailDelay()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.secondsLeft.collect {
                if (verifyFragment.isVisible) {
                    verifyFragment.setTime(it)
                }
            }
        }
    }
    private fun startMainActivity() {
        lifecycleScope.launchWhenStarted { viewModel.iRepository.getUser() }
        getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putBoolean(IS_FIRST_LAUNCH, true).apply()
        startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
        finish()

    }


    override fun onStart() {
        super.onStart()
        if (::verifyFragment.isInitialized && !verifyFragment.isDetached) {
            signIn()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
