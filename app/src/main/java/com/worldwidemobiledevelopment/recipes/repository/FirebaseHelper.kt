package com.worldwidemobiledevelopment.recipes.repository

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.worldwidemobiledevelopment.recipes.data.ResultWrapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.timerTask


@Singleton
class FirebaseHelper @Inject constructor() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun createUser(email: String, password: String): ResultWrapper<String> {
        // Create new user or throw exception
        return CompletableDeferred<ResultWrapper<String>>().apply {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseHelper", "User created")
                        CoroutineScope(IO).launch {
                            verifyUserEmail()
                        }
                        this.complete(ResultWrapper.success("Пользователь создан"))
                    } else {
                        when (task.exception) {
                            is FirebaseAuthException -> {
                                when (task.exception) {
                                    is FirebaseAuthWeakPasswordException -> {
                                        this.complete(
                                            ResultWrapper.error(
                                                "Пароль не отвечает требованиям безопасности. Минимальное количество символов - 6",
                                                task.exception
                                            )
                                        )
                                    }
                                    is FirebaseAuthUserCollisionException -> {
                                        this.complete(
                                            ResultWrapper.error(
                                                "Пользователь с такой почтой уже существует",
                                                task.exception
                                            )
                                        )
                                    }
                                    is FirebaseAuthInvalidCredentialsException -> {
                                        this.complete(
                                            ResultWrapper.error(
                                                "Неверно указана почта",
                                                task.exception
                                            )
                                        )
                                    }
                                    else -> {
                                        this.complete(
                                            ResultWrapper.error(
                                                "Произошла непредвиденная ошибка. Свяжитесь с нами и мы обязательно поможем!",
                                                task.exception
                                            )
                                        )
                                    }
                                }
                            }
                            is FirebaseNetworkException -> {
                                this.complete(
                                    ResultWrapper.error(
                                        "Требуется подключение к интернету",
                                        task.exception
                                    )
                                )
                            }
                            else -> {
                                this.complete(
                                    ResultWrapper.error(
                                        "Произошла непредвиденная ошибка. Свяжитесь с нами и мы обязательно поможем!",
                                        task.exception
                                    )
                                )
                            }
                        }
                    }
                }
        }.await()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): ResultWrapper<String> {
        // Sign in user or throw exception
        return CompletableDeferred<ResultWrapper<String>>().apply {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "User successfully signed in")
                    this.complete(ResultWrapper.success("Успешно"))
                } else {
                    when (task.exception) {
                        is FirebaseAuthException -> {
                            when (task.exception) {
                                is FirebaseAuthException -> {
                                    when (task.exception) {
                                        is FirebaseAuthWeakPasswordException -> {
                                            this.complete(
                                                ResultWrapper.error(
                                                    "Пароль не отвечает требованиям безопасности. Минимальное количество символов - 6",
                                                    task.exception
                                                )
                                            )
                                        }
                                        is FirebaseAuthUserCollisionException -> {
                                            this.complete(
                                                ResultWrapper.error(
                                                    "Пользователь с такой почтой уже существует",
                                                    task.exception
                                                )
                                            )
                                        }
                                        is FirebaseAuthInvalidCredentialsException -> {
                                            this.complete(
                                                ResultWrapper.error("Вы ввели неверный пароль", task.exception)
                                            )
                                        }
                                        is FirebaseAuthInvalidUserException -> {
                                            this.complete(
                                                ResultWrapper.error(
                                                    "Пользователя с такой почтой не существует",
                                                    task.exception
                                                )
                                            )
                                        }
                                        else -> {
                                            this.complete(
                                                ResultWrapper.error(
                                                    "Произошла непредвиденная ошибка. Свяжитесь с нами и мы обязательно поможем!",
                                                    task.exception
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is FirebaseNetworkException -> {
                            this.complete(ResultWrapper.error("Интерент подключение отсутсвует", task.exception))
                        }
                        is FirebaseTooManyRequestsException -> {
                            Log.e(
                                "AppDebug",
                                "FirebaseHelper:signInWithEmailAndPassword [${task.exception} |||||| ${(task.exception as FirebaseTooManyRequestsException).stackTrace.map { it }}]"
                            )
                            this.complete(
                                ResultWrapper.error(
                                    "Количество попыток ввода пароля истекло. Попробуйте позже или восстановите пароль",
                                    task.exception
                                )
                            )
                        }
                        else -> {
                            this.complete(ResultWrapper.error("Произошла непредвиденная ошибка", task.exception))
                        }
                    }
                }
            }
        }.await()
    }

    suspend fun signInAnAnonymously(): ResultWrapper<String> {
        return CompletableDeferred<ResultWrapper<String>>().apply {
            firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "User sign in anonymously")
                    this.complete(ResultWrapper.success("Пользователь вошел"))
                } else {
                    Log.e("FirebaseHelper", "Error while sign in anonymously : ${task.exception}")
                    this.complete(ResultWrapper.error("Ошибка входа", task.exception))
                }
            }
        }.await()
    }

    private suspend fun verifyUserEmail(): ResultWrapper<String> {
        return CompletableDeferred<ResultWrapper<String>>().apply {
            firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "Email verified")
                    this.complete(ResultWrapper.success("Почта подтверждена", null))
                } else {
                    Log.d("FirebaseHelper", "Waiting for verification...")
                    this.complete(ResultWrapper.error("Подтвердите Вашу почту", task.exception))
                }
            }
        }.await()
    }

    fun phoneNumberVerification(activity: Activity, phoneNumber: String, callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FirebaseHelper", "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("FirebaseHelper", "signInWithCredential:failure ${task.exception}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


//    private fun sendResetPasswordEmail() {
//        val email = (findViewById(R.id.reset_password_email) as EditText).text.toString()
//        firebaseAuth.sendPasswordResetEmail(email)
//            .addOnCompleteListener(this,
//                OnCompleteListener<Void?> { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(
//                            this@EmailPasswordAuthActivity,
//                            "Reset password code has been emailed to "
//                                    + email,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Log.e(
//                            TAG, "Error in sending reset password code",
//                            task.exception
//                        )
//                        Toast.makeText(
//                            this@EmailPasswordAuthActivity,
//                            "There is a problem with reset password, try later.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//    }

    suspend fun signUpWithGoogle(idToken: String) : ResultWrapper<FirebaseUser> {
        return CompletableDeferred<ResultWrapper<FirebaseUser>>().apply{
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task ->
                if (task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    this.complete(ResultWrapper.success(data = user, message = "Пользователь создан"))
                }else{
                    this.complete(ResultWrapper.error(message = "Не удалось войти с помощью Google", task.exception))
                }
            }
        }.await()
    }

    fun isUserVerified() = firebaseAuth.currentUser?.isEmailVerified ?: false

    fun isCurrentUserAnonymous() = firebaseAuth.currentUser?.isAnonymous

    fun removeUser() = firebaseAuth.currentUser?.delete()

    fun currentUser() = firebaseAuth.currentUser

    fun signOut() = FirebaseAuth.getInstance().signOut()

    private fun getUser() = firebaseAuth.currentUser

}