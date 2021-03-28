package com.worldwidemobiledevelopment.recipes.data

data class User (
    var id: String,
    var name: String,
    var status: UserStatus? = UserStatus.BEGINNER,
    var phoneNumber: String?,
    val links : UserLinks?,
    var email: String?,
    var avatar: String?,
    var bookedPostIds : HashMap<String, String>?,
    var recipes : List<RecipeRight>?,
    var followersIds : List<String>?,
    var followingsIds : List<String>?,
    var listOfNotifications : HashMap<String, Any>?,
){
    constructor() : this("", "", null, "", null, "", "", null, null, null, null, null)
}