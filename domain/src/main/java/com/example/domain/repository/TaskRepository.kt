package com.example.domain.repository

import android.util.Log
import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.network.dto.SubtaskPost
import com.example.network.dto.TaskDto
import com.example.network.dto.TaskPost
import com.example.network.dto.TaskStatusPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface TaskRepository {

    suspend fun populateTasksByProject(projectId: Int): Result<String, String>

    fun getTasksByProject(projectId: Int): Flow<List<TaskEntity>>

    fun getTaskById(taskId: Int): Flow<TaskEntity?>

    suspend fun populateTasks(): Result<String, String>

    fun getAllUserTasks(): Flow<List<TaskEntity>>

    suspend fun createTask(chosenProject: Int, task: TaskPost): Result<String, String>

    suspend fun createSubtask(taskId: Int, subtask: SubtaskPost): Result<String, String>

    suspend fun updateTask(
        taskId: Int,
        task: TaskPost,
        taskStatus: String
    ): Result<String, String>

    suspend fun updateTaskStatus(taskId: Int, taskStatus: String)

    suspend fun deleteTask(taskId: Int): Result<String, String>

}