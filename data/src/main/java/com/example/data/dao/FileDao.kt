package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.FileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {

    @Query("SELECT * FROM files")
    fun getAllFiles(): Flow<List<FileEntity>>

    @Query("SELECT * FROM files WHERE taskId = :taskId")
    fun getFilesByTaskId(taskId: Int): Flow<List<FileEntity>>

    @Query("SELECT * FROM files WHERE projectId = :projectId")
    fun getFilesByProjectId(projectId: Int): Flow<List<FileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFiles(fileList: List<FileEntity>?)

    @Query("DELETE FROM files WHERE id = :fileId")
    suspend fun deleteFile(fileId: Int)

    @Query("DELETE FROM files")
    suspend fun clearLocalCache()

}