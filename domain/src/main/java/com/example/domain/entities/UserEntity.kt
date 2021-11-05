package com.example.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey
    var id: String,
    var firstName: String?,
    var lastName: String?,
    val displayName : String = firstName+lastName,
    var email: String?,
    val avatarSmall : String? = null,
    val avatarMedium : String? = null,
    val profileUrl: String? = null,
    var chosen: Boolean? = null,
    var self: Boolean = false
)
