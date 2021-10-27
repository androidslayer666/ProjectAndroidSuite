package com.example.domain.entities

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
    val text: String = "",
    val parentId: String? = null,
    val inactive: Boolean? = null,
    val canEdit: Boolean? = null,
    val created: Date = Date(),
    val updated: Date =Date(),
    val taskId: Int? = null,
    val messageId: Int? =null,
    var commentLevel: Int  = 0
    )



