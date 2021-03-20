package com.worldwidemobiledevelopment.recipes.data

data class CookingStep (
    val description: String,
    val stepTime: Int? = null,
    val imageUrlSet: List<String>? = null
)