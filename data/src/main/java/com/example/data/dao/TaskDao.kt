package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM task")
    fun getAllFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE responsibles LIKE :userId")
    fun getUserTasks(userId: Int) : Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(taskList: List<TaskEntity>)

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskFromById(taskId: Int) : Flow<TaskEntity>

    @Query("SELECT * FROM task WHERE projectOwnerid = :projectId")
    fun getTasksWithProject(projectId: Int) : Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE projectOwnerid = :projectId")
    fun getTasksWithUser(projectId: Int) : Flow<List<TaskEntity>>

    @Query("DELETE FROM task WHERE id = :taskId")
    fun deleteTask(taskId: Int)

    @Query("DELETE FROM task")
    fun clearLocalCache()

}