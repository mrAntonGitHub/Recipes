package com.worldwidemobiledevelopment.recipes.view.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.ResultWrapper
import com.worldwidemobiledevelopment.recipes.data.room.entity.User
import com.worldwidemobiledevelopment.recipes.repository.IRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

const val RESEND_VERIFICATION_EMAIL_DELAY = 60

interface IRegistration{
    suspend fun signUp(email: String, password: String): ResultWrapper<User>
    suspend fun signIn(email: String, password: String): ResultWrapper<User>
    suspend fun signInWithGoogle(idToken: String): ResultWrapper<User>
    suspend fun signInWithFacebook(): ResultWrapper<User>

    suspend fun verifyUserEmail(): ResultWrapper<Nothing>
    suspend fun isEmailVerified(): ResultWrapper<Boolean>
    suspend fun resetPassword(email: String): ResultWrapper<Nothing>
}

@Singleton
class RegistrationViewModel : ViewModel(), IRegistration{

    @Inject
    lateinit var iRepository: IRepository

    init { Application.application.appComponent.inject(this) }

    private val _secondsLeft = MutableStateFlow(RESEND_VERIFICATION_EMAIL_DELAY)
    val secondsLeft = _secondsLeft.asStateFlow()

    suspend fun sendVerificationEmailDelay(){
        if (secondsLeft.value == 0 || secondsLeft.value == RESEND_VERIFICATION_EMAIL_DELAY){
            for (seconds in RESEND_VERIFICATION_EMAIL_DELAY downTo 0) {
                _secondsLeft.emit(seconds)
                delay(1_000)
            }
        }
    }

    override suspend fun signUp(email: String, password: String): ResultWrapper<User> {
        return iRepository.signUp(email, password)
    }

    override suspend fun signIn(email: String, password: String): ResultWrapper<User> {
        return iRepository.signIn(email, password)
    }

    override suspend fun signInWithGoogle(idToken: String): ResultWrapper<User> {
        return iRepository.signIn(idToken)
    }

    override suspend fun signInWithFacebook(): ResultWrapper<User> {
        return ResultWrapper.error()
        // TODO
    }

    override suspend fun verifyUserEmail(): ResultWrapper<Nothing> {
        return if (secondsLeft.value == RESEND_VERIFICATION_EMAIL_DELAY){
            iRepository.verifyUserEmail()
        }else{
            Log.d("RegistrationViewModel", "Waiting for ${RESEND_VERIFICATION_EMAIL_DELAY - secondsLeft.value} seconds to send verification email...")
            ResultWrapper.error()
        }

    }

    override suspend fun resetPassword(email: String): ResultWrapper<Nothing> {
        return iRepository.resetPassword(email)
    }

    override suspend fun isEmailVerified() = iRepository.isEmailVerified()
}