package com.example.domain.dto

import com.google.gson.annotations.SerializedName

data class SubtaskDto(
    @SerializedName("canEdit")
    val canEdit: Boolean? = null,
    @SerializedName("taskId")
    val taskId: Int,
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
    @SerializedName("updatedBy")
    val updatedBy: UserDto? = null,
    @SerializedName("created")
    val created: String? = null,
    @SerializedName("createdBy")
    val createdBy: UserDto? = null,
    @SerializedName("updated")
    val updated: String? = null

)

data class SubtaskPost(
    @SerializedName("responsible")
    val responsible: String,
    @SerializedName("title")
    val title: String
)


