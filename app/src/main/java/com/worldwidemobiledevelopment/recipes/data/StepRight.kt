package com.worldwidemobiledevelopment.recipes.data

data class StepRight (
    val description: String,
    val step_time: Int?,
    val step_image: String?
){
    constructor() : this("", null, "")
}