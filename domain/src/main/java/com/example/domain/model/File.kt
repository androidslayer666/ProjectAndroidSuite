package com.example.domain.model

import java.util.*



data class File (
    val folderId: Int? = null,
    val version: Int? = null,
    val versionGroup: Int? = null,
    val contentLength: String? = null,
    val pureContentLength: Int? = null,
    val fileStatus: Int? = null,
    val viewUrl: String? = null,
    val fileType: Int? = null,
    val fileExst: String? = null,
    val comment: String? = null,
    val id: Int,
    val title: String? = null,
    val access: Int? = null,
    val shared: Boolean? = null,
    val rootFolderType: Int? = null,
    val updatedBy: User? = null,
    val created: Date? = null,
    val createdBy: User? = null,
    val updated: Date? = null,
    val taskId: Int? = null,
    val projectId: Int? = null
)
