package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "milestones")
data class MilestoneEntity(
    val canEdit: Boolean? = null,
    val canDelete: Boolean? = null,
    @PrimaryKey
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    @Embedded(prefix = "projectOwner")
    val projectOwner: UserEntity? = null,
    val deadline: Date? = null,
    val isKey: Boolean? = null,
    val isNotify: Boolean? = null,
    val activeTaskCount: Int? = null,
    val closedTaskCount: Int? = null,
    val status: Int? = null,
    @Embedded(prefix = "responsible")
    val responsible: UserEntity? = null,
    @Embedded(prefix = "updatedBy")
    val updatedBy: UserEntity? = null,
    val created: Date? = null,
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val updated: Date? = null,
    val projectId: Int? = null
)