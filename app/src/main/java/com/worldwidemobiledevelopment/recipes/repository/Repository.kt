package com.worldwidemobiledevelopment.recipes.repository

import com.google.gson.Gson
import com.worldwidemobiledevelopment.recipes.data.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    fun getFoo(): Flow<List<String>> {
        return flow {
            val list = getList()
            emit(list)
        }.flowOn(IO)
    }

    suspend fun getList(): List<String> {
        delay(5_000)
        return listOf("1", "2", "3", "4", "5")
    }


    fun Recipe.toRecipeRight(): RecipeRight {

        val steps = mutableListOf<StepRight>()
        this.steps.forEach {
            steps.add(StepRight(it.description, it.stepTime, null))
        }

        //text = грамм:400,ккал:1012.0,белки:66.76,жиры:80.76,углеводы:0.0
        val split = this.caloriesInfoPerServing.split("[:,]".toRegex())

        return RecipeRight(
            this.name,
            this.description,
            steps,
            this.portionsNumber,
            this.additionalInfo,
            this.complexity,
            this.tags,
            this.ingredientsList,
            this.tagsDiet,
            this.cookingMinutes,
            split[1],
            split[3],
            split[5],
            split[7],
            split[9],
            null,
            0,
            User(),
            null,
            null
        )

    }

}