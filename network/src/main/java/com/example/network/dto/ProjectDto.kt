package com.example.network.dto

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class ProjectDto (

    @SerializedName("canEdit")
    val canEdit: Boolean? = null,
    @SerializedName("canDelete")
    val canDelete: Boolean? = null,
    @SerializedName("projectFolder")
    val projectFolder: Int? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("responsible")
    val responsible: UserDto? = null,
    @SerializedName("isPrivate")
    val isPrivate: Boolean? = null,
    @field:Json(name = "taskCount")
    @SerializedName("taskCount")
    val taskCount: Int,
    @SerializedName("taskCountTotal")
    val taskCountTotal: Int,
    @SerializedName("milestoneCount")
    val milestoneCount: Int? = null,
    @SerializedName("discussionCount")
    val discussionCount: Int? = null,
    @SerializedName("participantCount")
    val participantCount: Int? = null,
    @SerializedName("documentsCount")
    val documentsCount: Int? = null,
    @SerializedName("isFollow")
    val isFollow: Boolean? = null,
    @SerializedName("updatedBy")
    val updatedBy: UserDto? = null,
    @SerializedName("created")
    val created: String? = null,
    @SerializedName("createdBy")
    val createdBy: UserDto? = null,
    @SerializedName("updated")
    val updated: String? = null,
    var team: MutableList<UserDto>? = null,

    )

data class ProjectPost (
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("responsibleId")
    val responsibleId: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("private")
    val private: Boolean,
    @SerializedName("tasks")
    val taskDtos: List<TaskDto>,
    @SerializedName("milestones")
    val milestoneDtos:List<MilestoneDto>,
    @SerializedName("participants")
    val participants: List<String>? = null
)
