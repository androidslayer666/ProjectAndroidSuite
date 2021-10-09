package com.example.domain.repository

import com.example.database.entities.FileEntity
import com.example.domain.mappers.toListEntities
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    suspend fun populateTaskFiles(taskId: Int): Result<String, String>

    suspend fun populateProjectFiles(projectId: Int): Result<String, String>

    fun getFilesWithTaskId(taskId: Int): Flow<List<FileEntity>>

    fun getFilesWithProjectId(projectId: Int): Flow<List<FileEntity>>

    fun getAllFiles(): Flow<List<FileEntity>>
}