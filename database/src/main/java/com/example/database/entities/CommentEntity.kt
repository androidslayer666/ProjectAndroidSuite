package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey
    val id: String,
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val text: String? = null,
    val parentId: String? = null,
    val inactive: Boolean? = null,
    val canEdit: Boolean?,
    val created: Date? = null,
    val updated: Date? = null,
    val taskId: Int? = null,
    val messageId: Int? =null
    )

