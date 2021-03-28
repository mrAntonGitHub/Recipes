package com.worldwidemobiledevelopment.recipes.view.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.MainActivity
import com.worldwidemobiledevelopment.recipes.R
import com.worldwidemobiledevelopment.recipes.repository.FirebaseHelper
import com.worldwidemobiledevelopment.recipes.view.registration.RegistrationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Splash screen with Registration check:
 * Open registration activity for the first launch, for next launches open MainActivity
 **/

const val APP_PREFERENCES_NAME = "APP_PREFERENCES_NAME"
const val IS_FIRST_LAUNCH = "FirstLaunch"

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.application.appComponent.inject(this)
        setContentView(R.layout.activity_splash)

        CoroutineScope(IO).launch {
            val appPreferences = getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
            withContext(Main) {
                if (appPreferences.getBoolean(IS_FIRST_LAUNCH, false)) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, RegistrationActivity::class.java))
                    finish()
                }
            }
        }
    }
}