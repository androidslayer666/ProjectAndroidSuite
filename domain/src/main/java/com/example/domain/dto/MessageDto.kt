package com.example.domain.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("canCreateComment")
    val canCreateComment: Boolean? = null,
    @SerializedName("canEdit")
    val canEdit: Boolean? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("projectOwner")
    val projectOwner: UserDto? = null,
    @SerializedName("commentsCount")
    val commentsCount: Int? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("updatedBy")
    val updatedBy: UserDto? = null,
    @SerializedName("created")
    val created: String? = null,
    @SerializedName("createdBy")
    val createdBy: UserDto? = null,
    @SerializedName("updated")
    val updated: String? = null,
)

data class MessagePost(
    @SerializedName("projectid")
    val projectId: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("participants")
    val participants: String? = null,
    @SerializedName("notify")
    val notify: Boolean? = null,

)