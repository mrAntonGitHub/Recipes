package com.worldwidemobiledevelopment.recipes.data

data class Comment (
    val commentAuthorId: String,
    var commentId: String,
    val commentAuthorName: String,
    val commentAuthorAvatar: String?,
    val commentAuthorStatus: String,
    val commentText: String,
    val rating: Int, // Рейтинг который поставил пользователь блюду
    val date: String
)