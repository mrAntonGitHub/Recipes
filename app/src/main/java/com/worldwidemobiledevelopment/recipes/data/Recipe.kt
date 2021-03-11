package com.worldwidemobiledevelopment.recipes.data


import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("additional_info")
    val additionalInfo: String,
    @SerializedName("calories_info_per_serving")
    val caloriesInfoPerServing: String,
    @SerializedName("complexity")
    val complexity: Int,
    @SerializedName("cooking_minutes")
    val cookingMinutes: Double,
    @SerializedName("cooking_time")
    val cookingTime: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("ingredients")
    val ingredients: String,
    @SerializedName("ingredients_list")
    val ingredientsList: List<Map<String, String>>,
    @SerializedName("name")
    val name: String,
    @SerializedName("portions_number")
    val portionsNumber: String,
    @SerializedName("steps")
    val steps: List<Step>,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("tags_auto")
    val tagsAuto: List<String>,
    @SerializedName("tags_diet")
    val tagsDiet: List<String>,
    val icon: Int
)