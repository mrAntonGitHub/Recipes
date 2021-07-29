package com.worldwidemobiledevelopment.recipes.view.profile.newRecipe

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.CookingStep
import com.worldwidemobiledevelopment.recipes.data.Ingredient
import com.worldwidemobiledevelopment.recipes.data.Recipe
import com.worldwidemobiledevelopment.recipes.repository.IRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewRecipeViewModel : ViewModel() {

    @Inject lateinit var repository: IRepository

    init {
        Application.application.appComponent.inject(this)
    }

    var ingredients = mutableListOf<Ingredient>()


    var steps = mutableListOf<CookingStep>()


    var chosenUris = mutableListOf<Uri>()


    fun publishRecipe(recipe: Recipe){
        viewModelScope.launch {
            repository.publishRecipe(recipe)
        }
    }

    fun editRecipe(){
        // TODO edit recipe on Firestore
    }




}