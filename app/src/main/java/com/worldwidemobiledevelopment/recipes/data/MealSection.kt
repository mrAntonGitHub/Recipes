package com.worldwidemobiledevelopment.recipes.data


import com.google.gson.annotations.SerializedName

data class MealSection(
    @SerializedName("name")
    val name: String,
    @SerializedName("recipes")
    val recipes: List<Recipe>,
    @SerializedName("tags")
    val tags: String
)