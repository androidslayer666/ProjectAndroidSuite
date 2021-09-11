package com.example.domain.mappers

import android.util.Log
import com.example.database.entities.TaskEntity
import com.example.network.dto.TaskDto

fun List<TaskDto>.toListEntities(): List<TaskEntity> {

        val listProjectEntity = mutableListOf<TaskEntity>()
        for (taskDto in this) {

            listProjectEntity.add(
                taskDto.toEntity( )
            )
        }
        Log.d("TaskDto", listProjectEntity.toString())
        return listProjectEntity
    }

fun TaskDto.toEntity(): TaskEntity {
    return  TaskEntity(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        responsible = this.responsible?.toUserEntity(),
        updatedBy = this.updatedBy?.toUserEntity(),
        created = this.created?.stringToDate(),
        createdBy = this.createdBy?.toUserEntity(),
        updated = this.updated?.stringToDate(),
        milestoneId = this.milestoneId,
        deadline = this.deadline?.stringToDate(),
        subtasks = this.subtasks?.toSubtaskEntity(),
        responsibles = this.responsibles?.toListUserEntity()?.toMutableList(),
        projectOwner = this.projectOwner?.toProjectEntity(),
    )
}