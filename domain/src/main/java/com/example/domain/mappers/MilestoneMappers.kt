package com.example.domain.mappers

import android.util.Log
import com.example.database.entities.MessageEntity
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.domain.model.Message
import com.example.domain.model.Milestone
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
        isKey = this.isKey,
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


fun Milestone.toMilestonePost(): MilestonePost {
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

fun List<MilestoneEntity>.fromListMilestoneEntitiesToListMilestone(): MutableList<Milestone> {
    val listFiles = mutableListOf<Milestone>()
    listFiles.addAll(this.map { it.fromMilestoneEntityToMilestone() })
    return listFiles
}


fun MilestoneEntity.fromMilestoneEntityToMilestone(): Milestone {
    return Milestone(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        deadline = this.deadline,
        id = this.id,
        isKey = this.isKey,
        title = this.title,
        description = this.description,
        status = this.status,
        responsible = this.responsible?.fromUserEntityToUser(),
        updatedBy = this.updatedBy?.fromUserEntityToUser(),
        created = this.created,
        createdBy = this.createdBy?.fromUserEntityToUser(),
        updated = this.updated,
        projectId = projectId
    )
}