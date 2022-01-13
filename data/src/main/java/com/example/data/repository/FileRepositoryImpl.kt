package com.example.data.repository

import com.example.data.dao.FileDao
import com.example.domain.utils.Result
import com.example.domain.mappers.fromListFileEntitiesToListFiles
import com.example.domain.mappers.toListEntities
import com.example.domain.model.File
import com.example.domain.repository.FileRepository
import com.example.data.endpoints.FileEndPoint
import com.example.domain.entities.FileEntity
import com.example.domain.mappers.fromListFileEntitiesToListIds
import com.example.domain.utils.networkCaller
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepositoryImpl @Inject constructor(
    private val fileDao: FileDao,
    private val fileEndPoint: FileEndPoint
) : FileRepository {

    override suspend fun populateTaskFiles(taskId: Int): Result<String, Throwable> {
        return networkCaller(
            call = { fileEndPoint.getTaskFiles(taskId).listFiles?.toListEntities(taskId = taskId) },
            onSuccess = { files ->
                if (!files.isNullOrEmpty()) {
                    deleteTaskFileFromDbIfDeletedOnServer(
                        taskId = taskId,
                        files = files
                    )
                    fileDao.insertFiles(files)
                }
            }
        )
    }

    private suspend fun deleteTaskFileFromDbIfDeletedOnServer(
        taskId: Int,
        files: List<FileEntity>
    ) {
        fileDao.getFilesByTaskId(taskId).collectLatest { localFiles ->
            localFiles.forEach {
                if (!files.fromListFileEntitiesToListIds().contains(it.id)) {
                    fileDao.deleteFile(it.id)
                }
            }
        }
    }


    override suspend fun populateProjectFiles(projectId: Int): Result<String, Throwable> {
        return networkCaller(
            call = {
                fileEndPoint.getProjectFiles(projectId).filesSubTransporter?.listFiles?.toListEntities(
                    projectId
                )
            },
            onSuccess = { files ->
                if (!files.isNullOrEmpty()) {
                    deleteProjectFileFromDbIfDeletedOnServer(
                        projectId = projectId,
                        files = files
                    )
                    fileDao.insertFiles(files)
                }
            }
        )
    }

    private suspend fun deleteProjectFileFromDbIfDeletedOnServer(
        projectId: Int,
        files: List<FileEntity>
    ) {
        fileDao.getFilesByProjectId(projectId).collectLatest { localFiles ->
            localFiles.forEach {
                if (!files.fromListFileEntitiesToListIds().contains(it.id)) {
                    fileDao.deleteFile(it.id)
                }
            }
        }
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

    override suspend fun clearLocalCache() {
        fileDao.clearLocalCache()
    }

}