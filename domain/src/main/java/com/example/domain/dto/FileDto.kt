package com.example.domain.dto

import com.google.gson.annotations.SerializedName

data class FileDto (
    @SerializedName("folderId")
    val folderId: Int? = null,
    @SerializedName("version")
    val version: Int? = null,
    @SerializedName("versionGroup")
    val versionGroup: Int? = null,
    @SerializedName("contentLength")
    val contentLength: String? = null,
    @SerializedName("pureContentLength")
    val pureContentLength: Int? = null,
    @SerializedName("fileStatus")
    val fileStatus: Int,
    @SerializedName("viewUrl")
    val viewUrl: String? = null,
    @SerializedName("fileType")
    val fileType: Int? = null,
    @SerializedName("fileExst")
    val fileExst: String? = null,
    @SerializedName("comment")
    val comment: String? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("access")
    val access: Int? = null,
    @SerializedName("shared")
    val shared: Boolean? = null,
    @SerializedName("rootFolderType")
    val rootFolderType: Int? = null,
    @SerializedName("updatedBy")
    val updatedBy: UserDto? = null,
    @SerializedName("created")
    val created: String? = null,
    @SerializedName("createdBy")
    val createdBy: UserDto? = null,
    @SerializedName("updated")
    val updated: String? = null,
)
