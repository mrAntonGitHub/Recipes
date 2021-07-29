package com.worldwidemobiledevelopment.recipes.view.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.BookedRecipe
import com.worldwidemobiledevelopment.recipes.data.Recipe
import com.worldwidemobiledevelopment.recipes.data.ResultStatus
import com.worldwidemobiledevelopment.recipes.data.ResultWrapper
import com.worldwidemobiledevelopment.recipes.data.room.entity.User
import com.worldwidemobiledevelopment.recipes.data.room.entity.UserLevel
import com.worldwidemobiledevelopment.recipes.repository.IRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: IRepository

    init {
        Application.application.appComponent.inject(this)
    }


    suspend fun getUser() : com.worldwidemobiledevelopment.recipes.data.room.entity.User?{
        val user = repository.getUser()
        return if (user.status == ResultStatus.SUCCESS && user.data != null){
            user.data
        }else{
            null
        }
    }

    val recipes = listOf(
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
        Recipe(),
    )

    val bookedRecipes = listOf(
        BookedRecipe("", "", "", "", null, "", ""),
        BookedRecipe("", "", "", "", null, "", ""),
        BookedRecipe("", "", "", "", null, "", ""),
        BookedRecipe("", "", "", "", null, "", ""),
        BookedRecipe("", "", "", "", null, "", "")
    )

    fun getCurrentAndNextLevel(progress: Int): Pair<Pair<String, String>, Pair<Int, Int>>{
        /**
         * @return
         *  FirstPair: currentLevel and nextLevel
         *  SecondPair: minValuePoints and maxValuePoint in this level
         **/
        return when(progress){
            in UserLevel.BEGINNER.points -> {Pair(UserLevel.BEGINNER.title to UserLevel.ADEPT.title, UserLevel.BEGINNER.points.first to UserLevel.BEGINNER.points.last)}
            in UserLevel.ADEPT.points -> {Pair(UserLevel.ADEPT.title to UserLevel.EXPERT.title, UserLevel.ADEPT.points.first to UserLevel.ADEPT.points.last)}
            in UserLevel.EXPERT.points -> {Pair(UserLevel.EXPERT.title to UserLevel.SOU_CHEF.title, UserLevel.EXPERT.points.first to UserLevel.EXPERT.points.last)}
            in UserLevel.SOU_CHEF.points -> {Pair(UserLevel.SOU_CHEF.title to UserLevel.CHEF.title, UserLevel.SOU_CHEF.points.first to UserLevel.SOU_CHEF.points.last)}
            in UserLevel.CHEF.points -> {Pair(UserLevel.CHEF.title to UserLevel.LEGEND.title, UserLevel.CHEF.points.first to UserLevel.CHEF.points.last)}
            in UserLevel.LEGEND.points -> {Pair(UserLevel.LEGEND.title to "", UserLevel.LEGEND.points.first to UserLevel.LEGEND.points.last)}
            else -> {Pair(UserLevel.LEGEND.title to "", UserLevel.LEGEND.points.first to UserLevel.LEGEND.points.last)}
        }
    }

    suspend fun setAvatar(uri: Uri) : ResultWrapper<String>{
        return repository.setAvatar(uri)
    }

    suspend fun getRecipe(authorId: String, recipeId: String): ResultWrapper<Recipe> {
        return repository.getRecipe(authorId, recipeId)
    }
}