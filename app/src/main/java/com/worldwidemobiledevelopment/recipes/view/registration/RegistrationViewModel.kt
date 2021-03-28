package com.worldwidemobiledevelopment.recipes.view.registration

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.ResultWrapper
import com.worldwidemobiledevelopment.recipes.repository.FirebaseHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationViewModel : ViewModel() {

    @Inject
    lateinit var firebaseHelper: FirebaseHelper

    init {
        Application.application.appComponent.inject(this)
    }


    fun isUserVerified() = firebaseHelper.isUserVerified()

    suspend fun signUp(email: String, password: String): ResultWrapper<String> {
        return firebaseHelper.createUser(email, password)
    }

    suspend fun signInAnonymously() : ResultWrapper<String> {
        return firebaseHelper.signInAnAnonymously()
    }

    suspend fun signIn(email: String, password: String) : ResultWrapper<String>{
        return firebaseHelper.signInWithEmailAndPassword(email, password)
    }


    suspend fun signUpWithGoogle(idToken: String) : ResultWrapper<FirebaseUser>{
        return firebaseHelper.signUpWithGoogle(idToken)
    }


}