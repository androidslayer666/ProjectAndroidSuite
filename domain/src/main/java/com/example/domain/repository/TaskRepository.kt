package com.example.domain.repository

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Subtask
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun populateTasksByProject(projectId: Int): Result<String, String>

    fun getTasksByProject(projectId: Int): Flow<List<Task>>

    fun getTaskById(taskId: Int): Flow<Task?>

    suspend fun populateTasks(): Result<String, String>

    fun getAllTasks(): Flow<List<Task>>

    fun getUserTasks(userId: Int): Flow<List<Task>>

    suspend fun createTask(milestoneId: Int?, task: Task): Result<String, String>

    suspend fun createSubtask(subtask: Subtask): Result<String, String>

    suspend fun updateTask(
        taskId: Int,
        task: Task,
        taskStatus: String
    ): Result<String, String>

    suspend fun updateTaskStatus(taskId: Int, taskStatus: String): Result<String, String>

    suspend fun deleteTask(taskId: Int): Result<String, String>



}