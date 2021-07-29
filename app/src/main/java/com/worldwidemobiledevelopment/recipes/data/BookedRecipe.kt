package com.worldwidemobiledevelopment.recipes.data

data class BookedRecipe(
    var authorId: String,
    var mealId: String,
    var authorName: String,
    var mealName: String,

    var authorImage: String?,
    var mealImage: String?,
    var cookingTime: String?,
) // При нажатии проваливаемся в рецепт по authorId и mealId