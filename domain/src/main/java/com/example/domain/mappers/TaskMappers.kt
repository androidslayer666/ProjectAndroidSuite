package com.example.domain.mappers

import android.util.Log
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.network.dto.TaskDto
import java.util.*

fun List<TaskDto>.toListEntities(): List<TaskEntity> {

        val listProjectEntity = mutableListOf<TaskEntity>()
        for (taskDto in this) {

            listProjectEntity.add(
                taskDto.toEntity( )
            )
        }
        return listProjectEntity
    }

fun TaskDto.toEntity(): TaskEntity {
    return  TaskEntity(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        id = this.id,
        title = this.title ?: "",
        description = this.description ?: "",
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