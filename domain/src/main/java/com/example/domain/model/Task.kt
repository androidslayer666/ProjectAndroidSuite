package com.example.domain.model

import com.example.domain.filters.task.TaskStatus
import java.util.*


data class Task(
    val canEdit: Boolean? = null,
    val canCreateSubtask: Boolean? = null,
    val canCreateTimeSpend: Boolean? = null,
    val canDelete: Boolean? = null,
    val canReadFiles: Boolean? = null,
    val id: Int,
    var title: String = "",
    var description: String = "",
    var deadline: Date = Date(),
    val priority: Int? = null,
    val milestoneId: Int? = null,
    var projectOwner: Project? = null,
    val status: TaskStatus? = null,
    val responsible: User? = null,
    val updatedBy: User? = null,
    var created: Date = Date(),
    val createdBy: User? = null,
    val updated: Date = Date(),
    var responsibles: MutableList<User> = mutableListOf(),
    val projectId: Int? = null,
    val subtasks: List<Subtask>? = listOf()

)


