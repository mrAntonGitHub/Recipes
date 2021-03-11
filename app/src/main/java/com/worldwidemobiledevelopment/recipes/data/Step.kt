package com.worldwidemobiledevelopment.recipes.data


import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("description")
    val description: String,
    @SerializedName("step_time")
    val stepTime: Int
)