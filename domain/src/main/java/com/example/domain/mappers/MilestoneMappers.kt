package com.example.domain.mappers


import com.example.domain.Constants.FORMAT_API_DATE
import com.example.domain.dto.MilestoneDto
import com.example.domain.dto.MilestonePost
import com.example.domain.entities.MilestoneEntity
import com.example.domain.model.Milestone
import java.text.SimpleDateFormat
import java.util.*

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
        deadline = SimpleDateFormat(FORMAT_API_DATE, Locale.getDefault()).format(
            this.deadline ?: Date()
        ),
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


fun List<MilestoneDto>.toListMilestoneIds(): MutableList<Int> {
    val listString = mutableListOf<Int>()
    for (milestone in this) {
        listString.add(milestone.id)
    }
    return listString
}