package com.example.domain.repository

import android.util.Log
import com.example.database.dao.FileDao
import com.example.database.entities.CommentEntity
import com.example.database.entities.FileEntity
import com.example.domain.mappers.toCommentPost
import com.example.domain.mappers.toListEntities
import com.example.network.endpoints.FileEndPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val fileDao: FileDao,
    private val fileEndPoint: FileEndPoint
) {

    suspend fun populateTaskFiles(taskId: Int): Result<String, String> {
        return networkCaller(
            call = { fileEndPoint.getTaskFiles(taskId).listFiles?.toListEntities(taskId = taskId)},
            onSuccess = { files -> fileDao.insertTasks(files)  },
            onSuccessString = "Files downloaded successfully",
            onFailureString = "Having problem while downloading files, please check the network connection"
        )
    }

    suspend fun populateProjectFiles(projectId: Int): Result<String, String> {
        //Log.d("FileRepository", projectId.toString())
        return networkCaller(
            call = {
                fileEndPoint.getProjectFiles(projectId).listFiles?.listFiles?.toListEntities(
                    projectId
                )
            },
            onSuccess = { files -> fileDao.insertTasks(files)},
            onSuccessString = "Files downloaded successfully",
            onFailureString = "Having problem while downloading files, please check the network connection"
        )
    }

    fun getFilesWithTaskId(taskId: Int): Flow<List<FileEntity>> {
        return fileDao.getFilesByTaskId(taskId)
    }

    fun getFilesWithProjectId(projectId: Int): Flow<List<FileEntity>> {
        return fileDao.getFilesByProjectId(projectId)
    }

    fun getAllFiles(): Flow<List<FileEntity>> {
        return fileDao.getAllFiles()
    }

}