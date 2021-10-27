package com.example.domain.mappers

import com.example.domain.dto.SubtaskDto
import com.example.domain.entities.SubtaskEntity
import com.example.domain.model.Subtask

fun List<SubtaskDto>.toSubtaskEntity() : List<SubtaskEntity>{
    val list = mutableListOf<SubtaskEntity>()
    for (subtaskDto in this) {
        list.add(subtaskDto.toEntity())
    }
    return list
}

private fun SubtaskDto.toEntity(): SubtaskEntity {
    return SubtaskEntity(
        canEdit = this.canEdit,
        taskId = this.taskId,
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        created = this.created,
        createdBy = this.createdBy?.toUserEntity()
    )
}

fun SubtaskEntity.fromSubtaskEntityToSubtask() : Subtask {
    return Subtask(
        canEdit = this.canEdit,
        taskId = this.taskId,
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        created = this.created,
        createdBy = this.createdBy?.fromUserEntityToUser()
    )
}