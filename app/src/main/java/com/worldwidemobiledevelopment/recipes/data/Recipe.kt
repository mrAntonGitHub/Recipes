package com.worldwidemobiledevelopment.recipes.data

import android.net.Uri

data class Recipe (
    val id: String,
    val author: Follower? = null,
    val name : String,
    val images: List<String>? = null,
    val description: String? = null,
    val steps: List<CookingStep>,
    val portions_number : String? = null,
    val additional_info: String? = null,
    val complexity: Int? = null,
    val category : List<String>? = null,
    val ingredients_list : List<Ingredient>,
    val tags_diet: List<String>? = null,
    val cooking_minutes : String? = null,
    val grams : String? = null,
    val kcal: String? = null,
    val proteins: String? = null,
    val fats : String? = null,
    val carbohydrates : String? = null,
    val rating : Int? = null,
    val published_time : Long? = null,
    val comments : List<Comment>? = null
) {
    constructor() : this("", name = "", steps =  listOf(), ingredients_list = listOf())
}