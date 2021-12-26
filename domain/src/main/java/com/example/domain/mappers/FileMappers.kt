package com.example.domain.mappers

import com.example.domain.dto.FileDto
import com.example.domain.entities.FileEntity
import com.example.domain.model.File

fun List<FileDto>.toListEntities(taskId: Int? = null, projectId: Int? = null): List<FileEntity> {

    val listFileEntity = mutableListOf<FileEntity>()
    for (fileDto in this) {
        listFileEntity.add(
            fileDto.toEntity(taskId, projectId)
        )
    }
    return listFileEntity
}

fun FileDto.toEntity(taskId: Int?, projectId: Int?) : FileEntity{
    return FileEntity(
        id = this.id,
        title = this.title,
        updatedBy = this.updatedBy?.toUserEntity(),
        created = this.created?.stringToDate(),
        createdBy = this.createdBy?.toUserEntity(),
        updated = this.updated?.stringToDate(),
        fileExst = this.fileExst,
        taskId = taskId,
        projectId = projectId
    )
}


fun List<FileEntity>.fromListFileEntitiesToListFiles() : List<File>{
    val listFiles = mutableListOf<File>()
    listFiles.addAll(this.map { it.fromFileEntityToFile() })
    return listFiles
}

fun List<FileEntity>.fromListFileEntitiesToListIds() : List<Int>{
    return this.map { it.id }
}


fun FileEntity.fromFileEntityToFile(): File {
    return File(
        id = this.id,
        title = this.title,
        updatedBy = this.updatedBy?.fromUserEntityToUser(),
        created = this.created,
        createdBy = this.createdBy?.fromUserEntityToUser(),
        updated = this.updated,
        fileExst = this.fileExst,
        taskId = taskId,
        projectId = projectId
    )
}