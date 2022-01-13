package com.example.domain.repository

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.File
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    suspend fun populateTaskFiles(taskId: Int): Result<String, Throwable>

    suspend fun populateProjectFiles(projectId: Int): Result<String, Throwable>

    fun getFilesWithTaskId(taskId: Int): Flow<List<File>>

    fun getFilesWithProjectId(projectId: Int): Flow<List<File>>

    fun getAllFiles(): Flow<List<File>>

    suspend fun clearLocalCache()
}