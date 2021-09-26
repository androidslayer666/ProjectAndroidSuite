package com.example.database.dao

import androidx.room.*
import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM task")
    fun getAllFlow(): Flow<List<TaskEntity>>

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
    fun deleteAllTasks()

}