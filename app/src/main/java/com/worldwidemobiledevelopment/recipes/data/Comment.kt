package com.worldwidemobiledevelopment.recipes.data

data class Comment (
    val userId: String,
    val name: String,
    val status: String,
    val avatar: String?,
    val rating: Int,
    var commentId: String,
    val commentText: String,
    val date: String
)