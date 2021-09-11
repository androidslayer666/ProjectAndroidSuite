package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("createdBy")
    val createdBy: UserDto? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("parentId")
    val parentId: String? = null,
    @SerializedName("inactive")
    val inactive: Boolean? = null,
    @SerializedName("canEdit")
    val canEdit: Boolean? = null,
    @SerializedName("created")
    val created: String? = null,
    @SerializedName("updated")
    val updated: String? = null,

    )

data class CommentPost(
    @SerializedName("content")
    val content: String,
    @SerializedName("createdBy")
    val parentId: String

)