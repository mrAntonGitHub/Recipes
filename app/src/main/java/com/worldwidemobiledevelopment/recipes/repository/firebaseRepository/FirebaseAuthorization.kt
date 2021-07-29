package com.worldwidemobiledevelopment.recipes.repository.firebaseRepository

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.worldwidemobiledevelopment.recipes.data.ResultWrapper
import com.worldwidemobiledevelopment.recipes.data.UserType
import com.worldwidemobiledevelopment.recipes.data.room.entity.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

interface IFirebaseAuthorization {
    suspend fun userId() : ResultWrapper<String>

    suspend fun signUpWithEmailAndPassword(email: String, password: String): ResultWrapper<User>
    suspend fun signInWithEmailAndPassword(email: String, password: String): ResultWrapper<User>
    suspend fun signInWithGoogle(idToken: String): ResultWrapper<User>

    suspend fun verifyUserEmail(): ResultWrapper<Nothing>

    suspend fun isEmailVerified(): ResultWrapper<Boolean>
    suspend fun removeUserAuthorization(): ResultWrapper<Nothing>
    suspend fun resetPassword(email: String): ResultWrapper<Nothing>

    fun signOut()
}

/**
 * Using for authorization
 *  - SignIn / SignOut
 *  - SignUp (with Google, Facebook or with credentials)
 *  - Reset password
 *  - Verify email
 **/
const val FirebaseAuthWeakPasswordException = "Пароль не отвечает требованиям безопасности. Минимальное количество символов - 6"
const val FirebaseTooManyRequestsException = "Количество попыток ввода пароля истекло. Попробуйте еще раз через 3 минуты или восстановите пароль"
const val FirebaseAuthUnknownException = "Проверьте подключение к интернету. Попробуйте через некоторое время."
const val FirebaseAuthUserCollisionException = "Пользователь с такой почтой уже существует"
const val FirebaseAuthInvalidUserException = "Пользователя с такой почтой не существует"
const val FirebaseAuthFailToEnterUsingGoogle = "Не удалось войти с помощью Google"
const val FirebaseNetworkException = "Требуется подключение к интернету"
const val FirebaseAuthInvalidCredentialsException = "Неверные данные"

const val ExceptionEmailExpected = "Введите почту"
const val ExceptionPasswordExpected = "Введите пароль"
const val ExceptionConfirmPersonalDataProcessing = "Подтвердите согласие на обработку персональных данных"
const val ExceptionConfirmEmail = "Подтвердите адрес"

@Singleton
class FirebaseAuthorization @Inject constructor() : IFirebaseAuthorization {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun userId(): ResultWrapper<String> {
        val uid = firebaseAuth.currentUser?.uid
        return if (uid != null){
            ResultWrapper.success(data = uid)
        }else{
            ResultWrapper.error(exception = Exception("Can't get current user"))
        }
    }

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): ResultWrapper<User> {
        /**
         * Create user and directory
         * Send verification email
         * If exception return it
         * @return user if no exception happened
         **/
        return CompletableDeferred<ResultWrapper<User>>().apply {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseHelper", "User created")
                        CoroutineScope(IO).launch {
                            Log.d("FirebaseAuthorization", "Verifying email...")
                            verifyUserEmail()
                        }
                        this.complete(ResultWrapper.success(data = task.result?.user?.toUser()))
                    } else {
                        Log.e("FirebaseAuthorization", "Can't SignUp user due to exception: ${task.exception}")
                        val exceptionDescription = task.exception?.let { getErrorDescription(it) }
                        this.complete(ResultWrapper.error(message = exceptionDescription, exception = task.exception))
                    }
                }
        }.await()
    }
    override suspend fun signInWithEmailAndPassword(email: String, password: String): ResultWrapper<User> {
        /**
         * SignIn user or throw an exception
         **/
        return CompletableDeferred<ResultWrapper<User>>().apply {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "User signedIn successfully")
                    this.complete(ResultWrapper.success(data = task.result?.user?.toUser()))
                } else {
                    Log.e("FirebaseHelper", "Can't SignIn user due to exception: ${task.exception}")
                    val exceptionDescription = task.exception?.let { getErrorDescription(it) }
                    this.complete(ResultWrapper.error(message = exceptionDescription, exception = task.exception))
                }
            }
        }.await()
    }
    override suspend fun signInWithGoogle(idToken: String): ResultWrapper<User> {
        /**
         * SignIn using Google or @return an exception
         **/
        return CompletableDeferred<ResultWrapper<User>>().apply {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser?.uid != null) {
                        Log.d("FirebaseAuthorization", "User signIn successfully")
                        this.complete(ResultWrapper.success(data = task.result?.user?.toUser()))
                    } else {
                        Log.e("FirebaseHelper", "User's UID undefined")
                        this.complete(ResultWrapper.error(message = FirebaseAuthFailToEnterUsingGoogle, exception = task.exception))
                    }
                } else {
                    Log.e("FirebaseHelper", "Error while entering using Google: ${task.exception}")
                    this.complete(ResultWrapper.error(message = FirebaseAuthFailToEnterUsingGoogle, task.exception))
                }
            }
        }.await()
    }


    override suspend fun verifyUserEmail(): ResultWrapper<Nothing> {
        /**
         * Send user verification email or @return an exception
         **/
        return CompletableDeferred<ResultWrapper<Nothing>>().apply {
            firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "Verification email was sent")
                    this.complete(ResultWrapper.success("Письмо отправлено", null))
                } else {
                    Log.e("FirebaseHelper", "Exception while sending verification email", task.exception)
                    this.complete(ResultWrapper.error("Ошибка во время отправления сообщения", task.exception))
                }
            }
        }.await()
    }
    override suspend fun removeUserAuthorization(): ResultWrapper<Nothing> {
        return CompletableDeferred<ResultWrapper<Nothing>>().apply {
            firebaseAuth.currentUser?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("FirebaseAuthorization", "User was deleted")
                    this.complete(ResultWrapper.success())
                } else {
                    Log.e("FirebaseAuthorization", "Can't delete user due to exception: ${it.exception}")
                    this.complete(ResultWrapper.error(exception = it.exception))
                }
            }
        }.await()

    }
    override suspend fun isEmailVerified(): ResultWrapper<Boolean> {
        return CompletableDeferred<ResultWrapper<Boolean>>().apply {
            firebaseAuth.currentUser?.reload()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null) {
                        this.complete(ResultWrapper.success(data = currentUser.isEmailVerified))
                    } else {
                        Log.e("FirebaseAuthorization", "CurrentUser is null")
                        this.complete(ResultWrapper.error(exception = Exception("CurrentUser is null")))
                    }
                } else {
                    Log.e("FirebaseAuthorization", "${it.exception}")
                    this.complete(ResultWrapper.error(exception = it.exception))
                }
            }
        }.await()
    }
    override suspend fun resetPassword(email: String): ResultWrapper<Nothing> {
        return CompletableDeferred<ResultWrapper<Nothing>>().apply {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuthorization", "Reset email was sent")
                    this.complete(ResultWrapper.success())
                } else {
                    Log.d("FirebaseHelper", "Can't reset password due to exception: ${task.exception}")
                    val exceptionDescription = task.exception?.let { getErrorDescription(it) }
                    this.complete(ResultWrapper.error(message = exceptionDescription, exception = task.exception))
                }
            }
        }.await()
    }


    override fun signOut() = FirebaseAuth.getInstance().signOut()


    private fun getErrorDescription(exception: java.lang.Exception): String {
        /**
         * @return error message to display in UI
         **/
        return when (exception) {
            is FirebaseAuthException -> {
                return when (exception) {
                    is FirebaseAuthWeakPasswordException -> {
                        FirebaseAuthWeakPasswordException
                    }
                    is FirebaseAuthUserCollisionException -> {
                        FirebaseAuthUserCollisionException
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        FirebaseAuthInvalidCredentialsException
                    }
                    is FirebaseAuthInvalidUserException -> {
                        FirebaseAuthInvalidUserException
                    }
                    else -> {
                        Log.e("FirebaseAuthorization", "Undefined exception happened: $exception")
                        FirebaseAuthUnknownException
                    }
                }
            }
            is FirebaseTooManyRequestsException -> {
                FirebaseTooManyRequestsException
            }
            is FirebaseNetworkException -> {
                FirebaseNetworkException
            }
            else -> {
                Log.e("FirebaseAuthorization", "Undefined exception happened: $exception")
                FirebaseAuthUnknownException
            }
        }
    }
    private fun FirebaseUser.toUser(): User {
        return User(
            this.uid,
            this.displayName ?: "Пользователь #${Random.nextInt(100000, 999999)}",
            UserType.SIMPLE,
            this.email.toString(),
            this.photoUrl.toString(),
            0,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}