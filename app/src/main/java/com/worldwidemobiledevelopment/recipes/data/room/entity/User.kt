package com.worldwidemobiledevelopment.recipes.data.room.entity

import com.worldwidemobiledevelopment.recipes.data.*

data class User(
    var id: String,
    var name: String,
    var userType: UserType,
    var email: String,
    var avatar: String?,
    var progress: Int = 0,

    var recipes: List<Recipe>?,
    var bookedRecipes: List<BookedRecipe>?, // @example userId to mealId   1c22e2kck2kc2 to efji322fcaias

    val links: SocialNetworkLinks?,
    var followers: List<Follower>?,     // Те, кто подписаны
    var follows: List<Follower>?,    // На кого подписан

    var goodsInCart: List<MealInCart>?
) {
    constructor() : this("", "", UserType.SIMPLE, "","", 0, null, null, null, null, null, null)
}