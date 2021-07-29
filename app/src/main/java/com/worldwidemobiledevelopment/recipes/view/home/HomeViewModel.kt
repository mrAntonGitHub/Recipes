package com.worldwidemobiledevelopment.recipes.view.home

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.Recipe
import com.worldwidemobiledevelopment.recipes.data.StepRight
import com.worldwidemobiledevelopment.recipes.repository.Repository
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseAuthorization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
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
    lateinit var firebaseAuthorization: FirebaseAuthorization

    private val _b = MutableLiveData<List<String>>()
    val b: LiveData<List<String>>
        get() = _b

//    var menuAdapter: MenuAdapter? = null

    init {
        Application.application.appComponent.inject(this)

        val list = listOf<StepRight>(
            StepRight("adsa`dsasdads", null, null)
        )
//        val recipeRight = Recipe(
//            "Винегрет",
//            "Some description",
//            list,
//            ingredients_list = listOf(mapOf("Carrot" to "40 g"))
//        )
        //CoroutineScope(IO).launch { repository.firebaseRepository.pushRecipe(recipeRight) }
        CoroutineScope(IO).launch {
//            repository.firebaseRepository.getUsersRecipes()
        }

//        viewModelScope.launch {
//            repository.getFoo()
//                .collect {
//                    _b.value = it
//                }
//        }


    }

    fun verifyNumber(activity: Activity, number: String) {
    }


    override val verificationCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
        get() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.e("HomeViewModel.kt", "onVerificationCompleted")
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.e("HomeViewModel.kt", "onVerificationFailed")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                CoroutineScope(IO).launch {
                    for (i in 0..60) {
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
