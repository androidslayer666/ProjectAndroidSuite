package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "message")
data class MessageEntity(
    val canCreateComment: Boolean? = null,
    val canEdit: Boolean? = null,
    @PrimaryKey
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    @Embedded(prefix = "projectOwner")
    val projectOwner: UserEntity? = null,
    val commentsCount: Int? = null,
    val text: String? = null,
    val status: Int? = null,
    @Embedded(prefix = "updatedBy")
    val updatedBy: UserEntity? = null,
    val created: Date? = null,
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val updated: Date? = null,
    val projectId: Int? = null,
    var listMessages: List<CommentEntity>? = null
)
