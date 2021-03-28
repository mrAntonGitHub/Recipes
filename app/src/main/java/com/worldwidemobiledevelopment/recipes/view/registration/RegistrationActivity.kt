package com.worldwidemobiledevelopment.recipes.view.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.worldwidemobiledevelopment.recipes.MainActivity
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.data.ResultStatus
import com.worldwidemobiledevelopment.recipes.view.splash.APP_PREFERENCES_NAME
import com.worldwidemobiledevelopment.recipes.view.splash.IS_FIRST_LAUNCH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val GOOGLE_SIGN_IN: Int = 600

class RegistrationActivity : AppCompatActivity(), SignInFragment.SignInAction,
    SignUpEmailFragment.SignUpAction {

    lateinit var viewModel: RegistrationViewModel

    lateinit var signInFragment: SignInFragment
    lateinit var signUpEmailFragment: SignUpEmailFragment

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        signInFragment = SignInFragment(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, signInFragment, "SIGN_IN_FRAGMENT").commit()
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


    // SignIn
    override fun onSignIn(email: String, password: String) {
        CoroutineScope(IO).launch {
            val result = viewModel.signIn(email, password)
            if (result.status == ResultStatus.SUCCESS) {
                startMainActivity()
            } else {
                Log.e(
                    "RegistrationActivity.kt",
                    "Error while sign in : ${result.message} ${result.error}"
                )
                if (!signInFragment.isDetached) {
                    withContext(Main) {
                        result.message?.let { signInFragment.showError(it) }
                    }
                }
            }
        }
    }


    // SignUp
    override fun onSignUp() {
        signUpEmailFragment = SignUpEmailFragment(this)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.animator.slide_left_enter,
                R.animator.slide_left_exit,
                R.animator.slide_right_enter,
                R.animator.slide_right_exit
            )
            .addToBackStack("SIGN_UP_FRAGMENT")
            .replace(R.id.fragment, signUpEmailFragment, "SIGN_UP_FRAGMENT")
            .commit()
    }

    override fun signUp(email: String, password: String) {
        CoroutineScope(IO).launch {
            val result = viewModel.signUp(email, password)
            if (result.status == ResultStatus.SUCCESS) {
                startMainActivity()
            } else {
                Log.e("RegistrationActivity", "${result.message} : ${result.error}")
                if (!signUpEmailFragment.isDetached) {
                    withContext(Main) {
                        result.message?.let { signUpEmailFragment.showError(it) }
                    }
                }
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Get result from google signUp
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                CoroutineScope(IO).launch {
                    val result = viewModel.signUpWithGoogle(account.idToken!!)
                    if (result.status == ResultStatus.SUCCESS) {
                        startMainActivity()
                    } else {
                        Log.e(
                            "RegistrationActivity",
                            "Error while signing in with Google",
                            result.error
                        )
                        withContext(Main) {
                            result.message?.let { signInFragment.showError(it) }
                        }
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.e("RegistrationActivity", "Google sign in failed", e)
                signInFragment.showError("Не удалось войти с помощью Google аккаунта")
            }
        }
    }

    override fun onSignUpWithFacebook() {
        Toast.makeText(this, "onSignInWithFacebook", Toast.LENGTH_SHORT).show()
    }


    // Skip SignUp/SignIn
    override fun onSkipRegistration() {
        startMainActivity()
    }

    private fun startMainActivity() {
        getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(IS_FIRST_LAUNCH, true).apply()
        CoroutineScope(Main).launch {
            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
            finish()
        }
    }
}
