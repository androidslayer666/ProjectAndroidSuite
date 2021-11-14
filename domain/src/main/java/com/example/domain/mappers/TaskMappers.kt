package com.example.domain.mappers

import com.example.domain.Constants.FORMAT_API_DATE
import com.example.domain.dto.TaskDto
import com.example.domain.dto.TaskPost
import com.example.domain.entities.TaskEntity
import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Task
import java.text.SimpleDateFormat
import java.util.*

fun List<TaskDto>.toListEntities(): List<TaskEntity> {

    val listProjectEntity = mutableListOf<TaskEntity>()
    for (taskDto in this) {

        listProjectEntity.add(
            taskDto.toEntity()
        )
    }
    return listProjectEntity
}

fun TaskDto.toEntity(): TaskEntity {
    return TaskEntity(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        id = this.id,
        title = this.title ?: "",
        description = this.description ?: "",
        priority = this.priority,
        status = this.status,
        responsible = this.responsible?.toUserEntity(),
        updatedBy = this.updatedBy?.toUserEntity(),
        created = this.created?.stringToDate() ?: Date(),
        createdBy = this.createdBy?.toUserEntity(),
        updated = this.updated?.stringToDate() ?: Date(),
        milestoneId = this.milestoneId,
        deadline = this.deadline?.stringToDate() ?: Date(),
        subtasks = this.subtasks?.toSubtaskEntity(),
        responsibles = this.responsibles?.toListUserEntity()?.toMutableList() ?: mutableListOf(),
        projectOwner = this.projectOwner?.toProjectEntity(),
    )
}

fun Task.fromTaskEntityToPost(milestoneId: Int? = 0): TaskPost {
    return TaskPost(
        description = description,
        deadline = SimpleDateFormat(FORMAT_API_DATE, Locale.getDefault()).format(deadline),
        title = title,
        responsibles = responsibles.fromListUsersToStrings(),
        startDate = SimpleDateFormat(FORMAT_API_DATE, Locale.getDefault()).format(Date()),
        milestoneid = milestoneId?:0,
        priority = priority?.priorityToString(),
    )
}


fun Int.priorityToString(): String {
    return when (this) {
        0 -> "normal"
        1 -> "high"
        else -> "normal"
    }
}

fun List<TaskEntity>.fromListTaskEntitiesToListTasks(): List<Task> {
    val listFiles = mutableListOf<Task>()
    listFiles.addAll(this.map { it.fromTaskEntityToTask() })
    return listFiles
}


fun TaskEntity.fromTaskEntityToTask(): Task {
    return Task(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        id = this.id,
        title = this.title,
        description = this.description,
        priority = this.priority,
        status = this.status?.fromIntToTaskStatus(),
        responsible = this.responsible?.fromUserEntityToUser(),
        updatedBy = this.updatedBy?.fromUserEntityToUser(),
        created = this.created,
        createdBy = this.createdBy?.fromUserEntityToUser(),
        updated = this.updated,
        milestoneId = this.milestoneId,
        deadline = this.deadline,
        subtasks = this.subtasks?.map { it.fromSubtaskEntityToSubtask() },
        responsibles = this.responsibles.map { it.fromUserEntityToUser() }.toMutableList(),
        projectOwner = this.projectOwner?.fromProjectEntityToProject(),
    )
}

fun Int.fromIntToTaskStatus(): TaskStatus {
    return when (this) {
        1 -> TaskStatus.COMPLETE
        2 -> TaskStatus.ACTIVE
        else -> TaskStatus.ACTIVE
    }
}

fun TaskStatus.fromTaskStatusToString(): String {
    return when (this) {
        TaskStatus.COMPLETE -> "closed"
        TaskStatus.ACTIVE -> "open"
    }
}
