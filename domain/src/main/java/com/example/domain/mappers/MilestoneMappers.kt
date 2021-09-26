package com.example.domain.mappers

import android.util.Log
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.domain.repository.dateToString
import com.example.network.dto.MilestoneDto
import com.example.network.dto.MilestonePost
import com.example.network.dto.TaskDto

fun List<MilestoneDto>.toListEntities(projectId: Int): List<MilestoneEntity> {

    val listProjectEntity = mutableListOf<MilestoneEntity>()
    for (milestoneDto in this) {
        listProjectEntity.add(
            milestoneDto.toEntity(projectId)
        )
    }
    return listProjectEntity
}

fun MilestoneDto.toEntity(projectId: Int): MilestoneEntity {
    return MilestoneEntity(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        deadline = this.deadline?.stringToDate(),
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        responsible = this.responsible?.toUserEntity(),
        updatedBy = this.updatedBy?.toUserEntity(),
        created = this.created?.stringToDate(),
        createdBy = this.createdBy?.toUserEntity(),
        updated = this.updated?.stringToDate(),
        projectId = projectId
    )
}


fun MilestoneEntity.toMilestonePost(): MilestonePost {
    return MilestonePost(
        title = this.title,
        description = this.description,
        deadline = this.deadline?.dateToString(),
        responsible = this.responsible?.id,
        notifyResponsible = true,
        isNotify = true,
        isKey = true
        )
}