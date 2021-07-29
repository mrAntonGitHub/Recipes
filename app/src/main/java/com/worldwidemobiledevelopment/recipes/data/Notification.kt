package com.worldwidemobiledevelopment.recipes.data

data class Notification(
    val id: String,
    val mealId: String,
    val mealName: String,
    val mealImage: String?,
    val notificationType: NotificationType,
    val actionUserName: String,
    val actionUserId: String,  // User who did action
)