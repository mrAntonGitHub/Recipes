package com.worldwidemobiledevelopment.recipes.data

import android.content.Context
import android.content.res.Configuration
import com.worldwidemobiledevelopment.recipes.Application
import javax.inject.Inject

class SmallCardItem(recipe: Recipe, mealAction: MealAction) : CardItem(recipe, mealAction) {

    @Inject
    lateinit var context: Context

    init {
        Application.application.appComponent.inject(this)
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            spanCount / 2
        }else{
            spanCount / 4
        }

    }
}