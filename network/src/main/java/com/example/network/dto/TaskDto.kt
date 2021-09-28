package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("canEdit")
    val canEdit: Boolean? = null,
    @SerializedName("canCreateSubtask")
    val canCreateSubtask: Boolean? = null,
    @SerializedName("canCreateTimeSpend")
    val canCreateTimeSpend: Boolean? = null,
    @SerializedName("canDelete")
    val canDelete: Boolean? = null,
    @SerializedName("canReadFiles")
    val canReadFiles: Boolean? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("deadline")
    val deadline: String? = null,
    @SerializedName("priority")
    val priority: Int? = null,
    @SerializedName("milestoneId")
    val milestoneId: Int? = null,
    @SerializedName("projectOwner")
    val projectOwner: ProjectDto? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("responsible")
    val responsible: UserDto? = null,
    @SerializedName("updatedBy")
    val updatedBy: UserDto? = null,
    @SerializedName("created")
    val created: String? = null,
    @SerializedName("createdBy")
    val createdBy: UserDto? = null,
    @SerializedName("updated")
    val updated: String? = null,
    @SerializedName("responsibles")
    val responsibles: List<UserDto>? = null,
    @SerializedName("subtasks")
    val subtasks: List<SubtaskDto>? = null,

    )

data class TaskPost(
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("deadline")
    val deadline: String? = "",
    @SerializedName("priority")
    val priority: String? = "normal",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("milestoneid")
    val milestoneid: Int = 0,
    @SerializedName("responsibles")
    val responsibles: List<String>? = listOf(),
    @SerializedName("notify")
    val notify: Boolean? = false,
    @SerializedName("startDate")
    val startDate: String? = ""
)

data class TaskStatusPost(
    @SerializedName("status")
    val status: String,
    @SerializedName("statusId")
    val statusId: Int
)
