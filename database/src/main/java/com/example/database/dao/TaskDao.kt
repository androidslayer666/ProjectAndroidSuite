package com.example.database.dao

import androidx.room.*
import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(taskList: List<TaskEntity>)

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskFromById(taskId: Int) : Flow<TaskEntity>

    @Query("SELECT * FROM task WHERE projectOwnerid = :projectId")
    fun getTasksWithProject(projectId: Int) : Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE projectOwnerid = :projectId")
    fun getTasksWithUser(projectId: Int) : Flow<List<TaskEntity>>

    @Delete
    fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM task")
    fun deleteAllTasks()

}