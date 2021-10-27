package com.example.domain.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class SubtaskEntity(
    val canEdit: Boolean? = null,
    val taskId: Int,
    @PrimaryKey
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    val status: Int? = null,
    @Embedded(prefix = "responsible")
    val responsible: UserEntity? = null,
    @Embedded(prefix = "updatedBy")
    val updatedBy: UserEntity? = null,
    val created: String? = null,
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val updated: String? = null

)



