package com.worldwidemobiledevelopment.recipes.repository

import android.net.Uri
import android.util.Log
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.*
import com.worldwidemobiledevelopment.recipes.data.room.entity.User
import com.worldwidemobiledevelopment.recipes.repository.firebaseRepository.FirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton


interface IRepository{

    suspend fun getUser() : ResultWrapper<User>
    suspend fun getUser(uid: String) : ResultWrapper<User>
    suspend fun getUsers(vararg uid: String) : ResultWrapper<List<User>>
    suspend fun updateUser() : ResultWrapper<Nothing>

    suspend fun setAvatar(uri: Uri) : ResultWrapper<String>

    suspend fun getRecipe(authorId: String, recipeId: String) : ResultWrapper<Recipe>
    suspend fun publishRecipe(recipe: Recipe)

    /**
     * Authorization
     **/
    suspend fun signUp(email: String, password: String): ResultWrapper<User>
    suspend fun signIn(email: String, password: String): ResultWrapper<User>
    suspend fun signIn(idToken: String): ResultWrapper<User>
    suspend fun verifyUserEmail(): ResultWrapper<Nothing>
    suspend fun removeUser(): ResultWrapper<Nothing>
    suspend fun isEmailVerified(): ResultWrapper<Boolean>
    suspend fun resetPassword(email: String): ResultWrapper<Nothing>
    fun signOut()
}

@Singleton
class Repository @Inject constructor(private val firebaseRepository: FirebaseRepository) : IRepository {

    private var currentUser: User? = null
    private var isFirstLaunch = true

    init { Application.application.appComponent.inject(this) }

    override suspend fun getUser(): ResultWrapper<User> {
        Log.e("Repository", "1212")
        if (isFirstLaunch){
            isFirstLaunch = false
            Log.d("Repository", "Got by Firestore")
            val user = firebaseRepository.getUser()
            currentUser = user.data
            return user

        }else{
            Log.d("Repository", "Got from cash")
            return ResultWrapper.success(data = currentUser)
        }
    }

    override suspend fun getUser(uid: String): ResultWrapper<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(vararg uid: String): ResultWrapper<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(): ResultWrapper<Nothing> {
        val user = firebaseRepository.getUser()
        return if (user.status == ResultStatus.SUCCESS){
            currentUser = user.data
            ResultWrapper.success()
        }else{
            ResultWrapper.error()
        }

    }

    override suspend fun setAvatar(uri: Uri): ResultWrapper<String> {
        val currentUserId = currentUser?.id
        return if (currentUserId != null){
            firebaseRepository.setAvatar(uri, currentUserId)
        }else{
            ResultWrapper.error("Не удалось изменить картинку", exception = Exception("Current user Id is null"))

        }

    }

    override suspend fun getRecipe(authorId: String, recipeId: String): ResultWrapper<Recipe> {
        return firebaseRepository.getRecipe(authorId, recipeId)
    }

    override suspend fun publishRecipe(recipe: Recipe) {
        // TODO add user to recipe here
        firebaseRepository.publishRecipe(recipe)
    }


    /**
     * Authorization
     **/
    override suspend fun signUp(email: String, password: String): ResultWrapper<User> {
        return firebaseRepository.signUp(email, password)
    }
    override suspend fun signIn(email: String, password: String): ResultWrapper<User> {
        return firebaseRepository.signIn(email, password)

    }
    override suspend fun signIn(idToken: String): ResultWrapper<User> {
        return firebaseRepository.signIn(idToken)
    }
    override suspend fun verifyUserEmail(): ResultWrapper<Nothing> {
        return firebaseRepository.verifyUserEmail()
    }
    override suspend fun removeUser(): ResultWrapper<Nothing> {
        return firebaseRepository.removeUser()
    }
    override suspend fun isEmailVerified(): ResultWrapper<Boolean> {
        return firebaseRepository.isEmailVerified()
    }
    override suspend fun resetPassword(email: String): ResultWrapper<Nothing> {
        return firebaseRepository.resetPassword(email)
    }
    override fun signOut() {
        return firebaseRepository.signOut()
    }

//    fun RecipeConvert.toRecipeRight(): Recipe {
//        val steps = mutableListOf<StepRight>()
//        this.steps.forEach { steps.add(StepRight(it.description, it.stepTime, null)) }
//        //text = грамм:400,ккал:1012.0,белки:66.76,жиры:80.76,углеводы:0.0
//        val split = this.caloriesInfoPerServing.split("[:,]".toRegex())
//
//        return RecipeRight(
//            this.name,
//            this.description,
//            steps,
//            this.portionsNumber,
//            this.additionalInfo,
//            this.complexity,
//            this.tags,
//            this.ingredientsList,
//            this.tagsDiet,
//            this.cookingMinutes,
//            split[1],
//            split[3],
//            split[5],
//            split[7],
//            split[9],
//            null,
//            0,
//            User(),
//            null,
//            null,
//            null
//        )
//
//    }

}