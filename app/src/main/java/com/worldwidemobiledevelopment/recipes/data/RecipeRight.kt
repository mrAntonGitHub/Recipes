package com.worldwidemobiledevelopment.recipes.data

data class RecipeRight (

    val name : String,
    val description: String?,
    val steps: List<StepRight>,
    val portions_number : String?,
    val additional_info: String?,
    val complexity: Int?,
    val category : List<String>?,
    val ingredients_list : List<Map<String, String>>,
    val tags_diet: List<String>?,
    val cooking_minutes : Double?,
    val grams : String?,
    val kcal: String?,
    val proteins: String?,
    val fats : String?,
    val carbohydrates : String?,
    val image: List<String>?,
    val rating : Int?,
    val author: User?,
    val published_time : Long?,
    val comments : List<Comment>?

)