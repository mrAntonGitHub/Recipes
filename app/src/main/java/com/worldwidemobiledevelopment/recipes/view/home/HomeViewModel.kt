package com.worldwidemobiledevelopment.recipes.view.home

import android.app.Activity
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.MainActivity
import com.worldwidemobiledevelopment.recipes.repository.FirebaseHelper
import com.worldwidemobiledevelopment.recipes.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface FirebasePhoneAuthorization {
    val verificationCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
}

@Singleton
class HomeViewModel : ViewModel(), FirebasePhoneAuthorization {
    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var firebaseHelper: FirebaseHelper

    private val _b = MutableLiveData<List<String>>()
    val b : LiveData<List<String>>
        get() = _b

//    var menuAdapter: MenuAdapter? = null

    init {
        Application.application.appComponent.inject(this)
        Log.d("HomeViewModel", "${firebaseHelper.currentUser()}")
//        viewModelScope.launch {
//            repository.getFoo()
//                .collect {
//                    _b.value = it
//                }
//        }



    }

    fun verifyNumber(activity: Activity, number: String){
        firebaseHelper.phoneNumberVerification(activity, number, verificationCallback)
    }


    override val verificationCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
        get() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.e("HomeViewModel.kt", "onVerificationCompleted")
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("HomeViewModel.kt", "onVerificationFailed")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                // Code was sent
                Log.e("FirebaseHelper.kt", "onCodeSent")

                CoroutineScope(IO).launch {
                    for (i in 0..60){
                        delay(1_000)
                        Log.e("HomeViewModel.kt", i.toString())
                    }
                }
            }

            override fun onCodeAutoRetrievalTimeOut(string: String) {
                // TODO resend again if need
                Log.e("HomeViewModel.kt", string)
            }
        }


    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.e("FirebaseHelper.kt", "onVerificationCompleted")
            firebaseHelper.signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            if (exception is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.e("FirebaseHelper.kt", "onVerificationFailed Invalid request")
            } else if (exception is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.e("FirebaseHelper.kt", "onVerificationFailed The SMS quota for the project has been exceeded")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.



            Log.e("FirebaseHelper.kt", "onCodeSent")
            // Save verification ID and resending token so we can use them later

//            storedVerificationId = verificationId
//            resendToken = token
        }
    }

    // Dimensions
//    var smallItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_small_icon_height)
//    var mediumItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_middle_icon_height)
//    var longItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_long_icon_height)
//    var menuItemsSpace = context.resources.getDimensionPixelSize(R.dimen.menu_items_spaces)



//    fun initMenu(){
//        /* Setup menu items with proper height */
//
//        smallItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_small_icon_height)
//        mediumItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_middle_icon_height)
//        longItemHeight = context.resources.getDimensionPixelSize(R.dimen.menu_long_icon_height)
//        menuItemsSpace = context.resources.getDimensionPixelSize(R.dimen.menu_items_spaces)
//
//        menuItemsList.value = listOf(
//            MenuItem("Закуски", R.drawable.ic_snacks, smallItemHeight),
//            MenuItem("Сэндвичи", R.drawable.ic_sandwich, smallItemHeight),
//            MenuItem("Салаты", R.drawable.ic_salad, longItemHeight),
//            MenuItem("Супы", R.drawable.ic_soup, smallItemHeight),
//            MenuItem("Выпечка", R.drawable.ic_bakery, smallItemHeight),
//            MenuItem("Основные блюда", R.drawable.ic_main_dish, longItemHeight),
//            MenuItem("Десерты", R.drawable.ic_desert, mediumItemHeight),
//            MenuItem("Напитки", R.drawable.ic_beverage, mediumItemHeight)
//        )
//    }
//
//    fun initFakePopularMeals(){
//            popularMealsList = listOf(
//                PopularMeal(1, "Закуски", R.drawable.ic_snacks),
//                PopularMeal(2, "Сэндвичи", R.drawable.ic_sandwich),
//                PopularMeal(3, "Салаты", R.drawable.ic_salad),
//                PopularMeal(4, "Супы", R.drawable.ic_soup),
//                PopularMeal(5, "Выпечка", R.drawable.ic_bakery),
//                PopularMeal(6,"Основные блюда", R.drawable.ic_main_dish),
//                PopularMeal(7, "Десерты", R.drawable.ic_desert),
//                PopularMeal(8, "Напитки", R.drawable.ic_beverage),
//            )
//    }
}