package com.example.domain.model

import com.example.domain.utils.ProjectStatus
import java.util.*


data class Project(
    val canEdit: Boolean? = null,
    val canDelete: Boolean? = null,
    val projectFolder: Int? = null,
    var id: Int,
    var title: String = "",
    var description: String = "",
    val status: ProjectStatus? = null,
    var responsible: User? = null,
    val isPrivate: Boolean? = null,
    val taskCount: Int? = null,
    val taskCountTotal: Int? = null,
    val milestoneCount: Int? = null,
    val discussionCount: Int? = null,
    val participantCount: Int? = null,
    val documentsCount: Int? = null,
    val isFollow: Boolean? = null,
    val updatedBy: User? = null,
    val created: Date = Date(),
    val createdBy: User? = null,
    val updated: Date = Date(),
    var chosen: Boolean? = null,
    var team: MutableList<User>? = null
)


