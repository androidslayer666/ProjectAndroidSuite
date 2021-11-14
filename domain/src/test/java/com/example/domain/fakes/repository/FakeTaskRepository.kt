package com.example.domain.fakes.repository

import com.example.domain.fakes.model.DummyTask
import com.example.domain.fakes.model.FakeListTask
import com.example.domain.model.Subtask
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTaskRepository: TaskRepository {
    override suspend fun populateTasksByProject(projectId: Int): Result<String, String> {
        return Success("")
    }

    override fun getTasksByProject(projectId: Int): Flow<List<Task>> {
        return flow { emit(FakeListTask.list) }
    }

    override fun getTaskById(taskId: Int): Flow<Task?> {
        return flow { emit(DummyTask.task) }
    }

    override suspend fun populateTasks(): Result<String, String> {
        return Success("")
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return flow { emit(FakeListTask.list) }
    }

    override fun getUserTasks(userId: Int): Flow<List<Task>> {
        return flow { emit(FakeListTask.list) }
    }

    override suspend fun createSubtask(subtask: Subtask): Result<String, String> {
        return Success("")
    }

    override suspend fun updateTask(
        taskId: Int,
        task: Task,
        taskStatus: String
    ): Result<String, String> {
        return Success("")
    }

    override suspend fun updateTaskStatus(taskId: Int, taskStatus: String): Result<String, String> {
        return Success("")
    }

    override suspend fun deleteTask(taskId: Int): Result<String, String> {
        return Success("")
    }

    override suspend fun createTask(
        milestoneId: Int?,
        task: Task,
        projectId: Int
    ): Result<String, String> {
        return Success("")
    }
}