package com.example.domain.mappers

import com.example.database.entities.FileEntity
import com.example.network.dto.FileDto

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