package com.example.domain.mappers

import android.util.Log
import com.example.database.entities.FileEntity
import com.example.database.entities.MilestoneEntity
import com.example.network.dto.FileDto
import com.example.network.dto.MilestoneDto

fun List<FileDto>.toListEntities(taskId: Int): List<FileEntity> {

    val listFileEntity = mutableListOf<FileEntity>()
    for (fileDto in this) {
        listFileEntity.add(
            fileDto.toEntity(taskId)
        )
    }
    Log.d("TaskDto", listFileEntity.toString())
    return listFileEntity
}

fun FileDto.toEntity(taskId: Int) : FileEntity{
    return FileEntity(
        id = this.id,
        title = this.title,
        updatedBy = this.updatedBy?.toUserEntity(),
        created = this.created?.stringToDate(),
        createdBy = this.createdBy?.toUserEntity(),
        updated = this.updated?.stringToDate(),
        fileExst = this.fileExst,
        taskId = taskId
    )
}