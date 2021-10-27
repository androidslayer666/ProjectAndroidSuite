package com.example.domain.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "projects")
data class ProjectEntity(
    val canEdit: Boolean? = null,
    val canDelete: Boolean? = null,
    val projectFolder: Int? = null,
    @PrimaryKey
    var id: Int,
    var title: String = "",
    var description: String = "",
    val status: Int? = null,
    @Embedded(prefix = "userResponsible")
    var responsible: UserEntity? = null,
    val isPrivate: Boolean? = null,
    val taskCount: Int? = null,
    val taskCountTotal: Int? = null,
    val milestoneCount: Int? = null,
    val discussionCount: Int? = null,
    val participantCount: Int? = null,
    val documentsCount: Int? = null,
    val isFollow: Boolean? = null,
    @Embedded(prefix = "userUpdatedBy")
    val updatedBy: UserEntity? = null,
    val created: Date = Date(),
    @Embedded(prefix = "userCratedBy")
    val createdBy: UserEntity? = null,
    val updated: Date = Date(),
    var chosen: Boolean? = null,
    var team: MutableList<UserEntity>? = null
)


