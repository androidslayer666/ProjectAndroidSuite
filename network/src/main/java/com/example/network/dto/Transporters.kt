package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class ProjectsTransporter(
    @SerializedName("response")
    val listProjectDtos: List<ProjectDto>? = null
)

data class ProjectTransporter(
    @SerializedName("response")
    val projectDto: ProjectDto? = null
)

data class MilestonesTransporter(
    @SerializedName("response")
    val listMilestoneDtos: List<MilestoneDto>? = null
)

data class MessagesTransporter(
    @SerializedName("response")
    val listMessages: List<MessageDto>? = null
)

data class TasksTransporter(
    @SerializedName("response")
    val listTaskDto: List<TaskDto>? = null
)

data class TaskTransporter(
    @SerializedName("response")
    val taskDto: TaskDto? = null
)

data class FilesTransporter(
    @SerializedName("response")
    val listFiles: FilesSubTransporter? = null
)

data class FilesTransporterResponse(
    @SerializedName("response")
    val listFiles: List<FileDto>? = null
)

data class FilesSubTransporter(
    @SerializedName("files")
    val listFiles: List<FileDto>? = null
)



data class CommentsTransporter(
    @SerializedName("response")
    val listCommentDtos: List<CommentDto>? = null
)

data class SubtaskTransporter(
    @SerializedName("response")
    val listSubtaskDto: List<SubtaskDto>? = null
)

data class UserTransporter(
    @SerializedName("response")
    val user: UserDto? = null
)
