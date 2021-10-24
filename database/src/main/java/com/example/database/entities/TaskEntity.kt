package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
    val canEdit: Boolean? = null,
    val canCreateSubtask: Boolean? = null,
    val canCreateTimeSpend: Boolean? = null,
    val canDelete: Boolean? = null,
    val canReadFiles: Boolean? = null,
    @PrimaryKey
    val id: Int,
    var title: String = "",
    var description: String = "",
    var deadline: Date = Date(),
    val priority: Int? = null,
    val milestoneId: Int? = null,
    @Embedded(prefix = "projectOwner")
    var projectOwner: ProjectEntity? = null,
    val status: Int? = null,
    @Embedded(prefix = "responsible")
    val responsible: UserEntity? = null,
    @Embedded(prefix = "updatedBy")
    val updatedBy: UserEntity? = null,
    var created: Date = Date(),
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val updated: Date = Date(),
    var responsibles: MutableList<UserEntity> = mutableListOf(),
    val projectId: Int? = null,
    val subtasks: List<SubtaskEntity>? = listOf()
)


