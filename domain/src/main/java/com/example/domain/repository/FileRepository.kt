package com.example.domain.repository

import android.util.Log
import com.example.database.dao.FileDao
import com.example.database.entities.CommentEntity
import com.example.database.entities.FileEntity
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

    suspend fun getTaskFiles(taskId: Int) {
        Log.d("FileRepository", taskId.toString())
        val files = fileEndPoint.getTaskFiles(taskId).listFiles?.toListEntities(taskId)
        Log.d("FileRepository", files.toString())
        fileDao.insertTasks(files)
    }

    fun getFilesWithTaskId(taskId: Int): Flow<List<FileEntity>> {
        return fileDao.getFilesByTaskId(taskId)
    }

}