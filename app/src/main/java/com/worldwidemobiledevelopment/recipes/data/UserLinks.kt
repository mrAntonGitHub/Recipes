package com.worldwidemobiledevelopment.recipes.data

data class UserLinks (
    val instagramLink : String?,
    val vkLink : String,
    val facebookLink : String?,
    val tikTokLink : String?,
) {
    constructor() : this("", "","","")
}
