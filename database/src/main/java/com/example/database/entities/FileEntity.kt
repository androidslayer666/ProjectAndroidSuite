package com.example.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "files")
data class FileEntity (
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
    @PrimaryKey
    val id: Int,
    val title: String? = null,
    val access: Int? = null,
    val shared: Boolean? = null,
    val rootFolderType: Int? = null,
    @Embedded(prefix = "updatedBy")
    val updatedBy: UserEntity? = null,
    val created: Date? = null,
    @Embedded(prefix = "createdBy")
    val createdBy: UserEntity? = null,
    val updated: Date? = null,
    val taskId: Int? = null,
    val projectId: Int? = null
)
