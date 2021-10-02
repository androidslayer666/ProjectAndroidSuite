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

    suspend fun populateTaskFiles(taskId: Int) : Result<String, String>{
        try {
            //Log.d("FileRepository", taskId.toString())
            val files = fileEndPoint.getTaskFiles(taskId).listFiles?.toListEntities(taskId = taskId)
            //Log.d("FileRepository", files.toString())
            return if(files == null){
                Failure("can't update files sue to a server problem")
            }else {
                fileDao.insertTasks(files)
                Success("Files are populated")
            }
        } catch (e: java.lang.Exception) {
            Log.e("FileRepository", "caught an exception" + e.toString())
            return Failure("can't update files sue to a server problem")
        }
    }

    suspend fun populateProjectFiles(projectId: Int) : Result<String, String> {
        //Log.d("FileRepository", projectId.toString())
        try {
            val files =
                fileEndPoint.getProjectFiles(projectId).listFiles?.listFiles?.toListEntities(
                    projectId = projectId
                )
            return if(files == null) {
                Failure("can't update files sue to a server problem")
            }else{
                fileDao.insertTasks(files)
                Success("Files are populated")
            }

        } catch (e: Exception) {
            Log.e("FileRepository", "caught an exception" + e.toString())
            return Failure("can't update files sue to a server problem")
        }
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