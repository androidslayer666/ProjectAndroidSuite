package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class MilestoneDto(
    @SerializedName("canEdit")
    val canEdit: Boolean? = null,
    @SerializedName("canDelete")
    val canDelete: Boolean? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("projectOwner")
    val projectOwner: UserDto? = null,
    @SerializedName("deadline")
    val deadline: String? = null,
    @SerializedName("isKey")
    val isKey: Boolean? = null,
    @SerializedName("isNotify")
    val isNotify: Boolean? = null,
    @SerializedName("activeTaskCount")
    val activeTaskCount: Int? = null,
    @SerializedName("closedTaskCount")
    val closedTaskCount: Int? = null,
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
    val updated: String? = null,
    )

data class MilestonePost(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("deadline")
    val deadline: String? = null,
    @SerializedName("isKey")
    val isKey: Boolean? = null,
    @SerializedName("isNotify")
    val isNotify: Boolean? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("responsible")
    val responsible: String? = null,
    @SerializedName("notifyResponsible")
    val notifyResponsible: Boolean? = null

)
