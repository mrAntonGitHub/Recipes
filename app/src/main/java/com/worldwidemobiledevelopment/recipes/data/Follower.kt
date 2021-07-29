package com.worldwidemobiledevelopment.recipes.data

import com.worldwidemobiledevelopment.recipes.data.room.entity.UserLevel

data class Follower(
    var id: String,
    var name: String,
    var avatar: String? = null,
    var level: Int?
)