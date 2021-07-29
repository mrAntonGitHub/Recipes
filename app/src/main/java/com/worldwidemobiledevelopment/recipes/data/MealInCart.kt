package com.worldwidemobiledevelopment.recipes.data

data class MealInCart(
    val id: String,
    val authorId: String,
    val authorType: UserType,
    val name: String,
    val mealImage: String?,
    val ingredients: List<Map<String, String>>?
)