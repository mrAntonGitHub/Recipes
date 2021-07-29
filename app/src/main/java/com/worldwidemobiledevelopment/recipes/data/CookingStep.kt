package com.worldwidemobiledevelopment.recipes.data

import android.net.Uri

data class CookingStep (
    var description: String,
    var imageUrlSet: MutableList<String>? = mutableListOf()
)