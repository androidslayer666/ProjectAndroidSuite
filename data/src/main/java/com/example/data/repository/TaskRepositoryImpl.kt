package com.example.data.repository

import android.util.Log
import com.example.data.ResponseIsEmptyException
import com.example.data.dao.TaskDao
import com.example.data.endpoints.TaskEndPoint
import com.example.domain.dto.SubtaskPost
import com.example.domain.dto.TaskStatusPost
import com.example.domain.mappers.*
import com.example.domain.model.Subtask
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import com.example.domain.utils.networkCaller
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskEndPoint: TaskEndPoint
) : TaskRepository {

    override suspend fun populateTasksByProject(projectId: Int): Result<String, Throwable> {
        try {
            val tasksActive = taskEndPoint.getProjectTasks(projectId, "Open").listTaskDto
            tasksActive?.let {
                taskDao.insertTasks(it.toListEntities())
                return Success("")
            } ?: return Failure(ResponseIsEmptyException())
        } catch (e: Exception) {
            return Failure(e)
        }
    }


    override fun getTasksByProject(projectId: Int): Flow<List<Task>> {
        return taskDao.getTasksWithProject(projectId).transform {
            emit(it.fromListTaskEntitiesToListTasks())
        }
    }

    override fun getTaskById(taskId: Int): Flow<Task?> {
        return taskDao.getTaskFromById(taskId).transform {
            emit(it.fromTaskEntityToTask()) }
    }

    override suspend fun populateTasks(): Result<String, Throwable> {
        return networkCaller(
            call = { taskEndPoint.getUserTasks() },
            onSuccess = { tasks ->
                tasks.listTaskDto?.toListEntities()?.let { taskDao.insertTasks(it) }
            }
        )
    }

    override fun getUserTasks(userId: Int): Flow<List<Task>> {
        return taskDao.getUserTasks(userId).transform { emit(it.fromListTaskEntitiesToListTasks()) }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllFlow().transform { emit(it.fromListTaskEntitiesToListTasks()) }
    }

    override suspend fun createTask(milestoneId: Int?, task: Task, projectId: Int): Result<String, Throwable> {
        return networkCaller(
            call = {
                taskEndPoint.addTaskToProject(
                    projectId,
                    task.fromTaskEntityToPost(milestoneId)
                )
            },
            onSuccess = { populateTasks() }
        )
    }

    override suspend fun createSubtask(
        subtask: Subtask
    ): Result<String, Throwable> {
        return networkCaller(
            call = {
                taskEndPoint.createSubtask(
                    subtask.taskId,
                    SubtaskPost(
                        title = subtask.title ?: "",
                        responsible = subtask.responsible?.id ?: ""
                    )
                )
            },
            onSuccess = { populateTasks() },
        )
    }


    override suspend fun updateTask(
        taskId: Int,
        task: Task,
        taskStatus: String
    ): Result<String, Throwable> {
        return networkCaller(
            call = { taskEndPoint.updateTask(taskId, task.fromTaskEntityToPost()) },
            onSuccess = {
                populateTasks()
                taskEndPoint.updateTaskStatus(taskId, TaskStatusPost(taskStatus, 1))
            },
        )
    }

    override suspend fun updateTaskStatus(taskId: Int, taskStatus: String): Result<String, Throwable> {
        return networkCaller(
            call = { taskEndPoint.updateTaskStatus(taskId, TaskStatusPost(taskStatus, 1)) },
            onSuccess = {
                taskEndPoint.getTaskById(taskId).taskDto?.toEntity().let {
                    taskDao.insertTasks(listOf(it!!))
                }
            }
        )
    }


    override suspend fun deleteTask(taskId: Int): Result<String, Throwable> {
        return networkCaller(
            call = { taskEndPoint.deleteTask(taskId) },
            onSuccess = { populateTasks() }
        )
    }

    override suspend fun clearLocalCache() {
        taskDao.clearLocalCache()
    }

}