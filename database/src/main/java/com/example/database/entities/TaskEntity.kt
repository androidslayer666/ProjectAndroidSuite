package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.network.dto.UserDto
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
    val canEdit: Boolean? = null,
    val canCreateSubtask: Boolean? = null,
    val canCreateTimeSpend: Boolean? = null,
    val canDelete: Boolean? = null,
    val canReadFiles: Boolean? = null,
    @PrimaryKey
    override val id: Int,
    override var title: String? = null,
    var description: String? = null,
    var deadline: Date? = null,
    val priority: Int? = null,
    val milestoneId: Int? = null,
    @Embedded(prefix = "projectOwner")
    var projectOwner: ProjectEntity? = null,
    val status: Int? = null,
    @Embedded(prefix = "responsible")
    val responsible: UserEntity? = null,
    @Embedded(prefix = "updatedBy")
    val updatedBy: UserEntity? = null,
    var created: Date? = null,
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val updated: Date? = null,
    var responsibles: MutableList<UserEntity>? = null,
    val projectId: Int? = null,
    val subtasks: List<SubtaskEntity>?

) : UniversalEntity(id, title)


