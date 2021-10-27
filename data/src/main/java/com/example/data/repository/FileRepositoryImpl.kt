package com.example.data.repository

import com.example.data.dao.FileDao
import com.example.domain.Result
import com.example.domain.mappers.fromListFileEntitiesToListFiles
import com.example.domain.mappers.toListEntities
import com.example.domain.model.File
import com.example.domain.networkCaller
import com.example.domain.repository.FileRepository
import com.example.data.endpoints.FileEndPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepositoryImpl @Inject constructor(
    private val fileDao: FileDao,
    private val fileEndPoint: FileEndPoint
) : FileRepository {

    override suspend fun populateTaskFiles(taskId: Int): Result<String, String> {
        return networkCaller(
            call = { fileEndPoint.getTaskFiles(taskId).listFiles?.toListEntities(taskId = taskId) },
            onSuccess = { files -> fileDao.insertTasks(files) },
            onSuccessString = "Files downloaded successfully",
            onFailureString = "Having problem while downloading files, please check the network connection"
        )
    }

    override suspend fun populateProjectFiles(projectId: Int): Result<String, String> {
        //Log.d("FileRepository", projectId.toString())
        return networkCaller(
            call = {
                fileEndPoint.getProjectFiles(projectId).listFiles?.listFiles?.toListEntities(
                    projectId
                )
            },
            onSuccess = { files -> fileDao.insertTasks(files) },
            onSuccessString = "Files downloaded successfully",
            onFailureString = "Having problem while downloading files, please check the network connection"
        )
    }

    override fun getFilesWithTaskId(taskId: Int): Flow<List<File>> {
        return fileDao.getFilesByTaskId(taskId).transform { emit(it.fromListFileEntitiesToListFiles()) }
    }

    override fun getFilesWithProjectId(projectId: Int): Flow<List<File>> {
        return fileDao.getFilesByProjectId(projectId).transform { emit(it.fromListFileEntitiesToListFiles()) }
    }

    override fun getAllFiles(): Flow<List<File>> {
        return fileDao.getAllFiles().transform { emit(it.fromListFileEntitiesToListFiles()) }
    }

}