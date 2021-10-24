package com.example.domain.mappers

import com.example.database.entities.ProjectEntity
import com.example.domain.model.Project
import com.example.network.dto.ProjectDto
import com.example.network.dto.ProjectPost
import java.util.*

fun List<ProjectDto>.toListEntities(): MutableList<ProjectEntity> {
    val listProjectEntity = mutableListOf<ProjectEntity>()
    for (projectDto in this) {
        listProjectEntity.add(
            projectDto.toProjectEntity()
        )
    }
    return listProjectEntity
}

fun ProjectDto.toProjectEntity(): ProjectEntity {
    return ProjectEntity(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        id = this.id,
        title = this.title ?: "",
        description = this.description ?: "",
        status = this.status,
        responsible = this.responsible?.toUserEntity(),
        isPrivate = this.isPrivate,
        taskCount = this.taskCount,
        taskCountTotal = this.taskCountTotal,
        milestoneCount = this.milestoneCount,
        discussionCount = this.discussionCount,
        participantCount = this.participantCount,
        documentsCount = this.documentsCount,
        isFollow = this.isFollow,
        updatedBy = this.updatedBy?.toUserEntity(),
        created = this.created?.stringToDate() ?: Date(),
        createdBy = this.createdBy?.toUserEntity(),
        updated = this.updated?.stringToDate() ?: Date(),
        team = this.team?.toListUserEntity()
    )
}

fun Project.fromEntityToPost(): ProjectPost {
    return ProjectPost(
        title = this.title,
        description = this.description,
        responsibleId = this.responsible?.id.toString(),
        tags = "",
        private = true,
        taskDtos = listOf(),
        milestoneDtos = listOf(),
        participants = this.team?.fromListUsersToStrings() ?: listOf()
    )
}

fun List<ProjectEntity>.fromListProjectEntitiesToListProjects(): MutableList<Project> {
    val listProjects = mutableListOf<Project>()
    listProjects.addAll(this.map { it.fromProjectEntityToProject() })
    return listProjects
}


fun ProjectEntity.fromProjectEntityToProject(): Project {

    return Project(
        canEdit = this.canEdit,
        canDelete = this.canDelete,
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        responsible = this.responsible?.fromUserEntityToUser(),
        isPrivate = this.isPrivate,
        taskCount = this.taskCount,
        taskCountTotal = this.taskCountTotal,
        milestoneCount = this.milestoneCount,
        discussionCount = this.discussionCount,
        participantCount = this.participantCount,
        documentsCount = this.documentsCount,
        isFollow = this.isFollow,
        updatedBy = this.updatedBy?.fromUserEntityToUser(),
        created = this.created,
        createdBy = this.createdBy?.fromUserEntityToUser(),
        updated = this.updated,
        team = this.team?.map { it.fromUserEntityToUser() }?.toMutableList()
    )
}

fun List<ProjectEntity>.toListProjectIds(): MutableList<Int> {
    val listString = mutableListOf<Int>()
    for (project in this) {
        listString.add(project.id)
    }
    return listString
}



