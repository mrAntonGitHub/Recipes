package com.worldwidemobiledevelopment.recipes.data

data class Comment (
    val userId: String,
    val userName: String,
    val userStatus: String,
    val userAvatar: String?,
    val rating: Int,
    var commentId: String,
    val commentText: String,
    val date: String
)