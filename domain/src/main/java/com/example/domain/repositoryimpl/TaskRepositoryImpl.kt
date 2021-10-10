package com.example.domain.repositoryimpl

import android.util.Log
import com.example.database.dao.TaskDao
import com.example.database.entities.ProjectEntity
import com.example.database.entities.SubtaskEntity
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.mappers.*
import com.example.domain.model.Subtask
import com.example.domain.model.Task
import com.example.domain.repository.*
import com.example.network.dto.SubtaskPost
import com.example.network.dto.TaskDto
import com.example.network.dto.TaskPost
import com.example.network.dto.TaskStatusPost
import com.example.network.endpoints.ProjectEndPoint
import com.example.network.endpoints.TaskEndPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskEndPoint: TaskEndPoint,
    private val projectEndPoint: ProjectEndPoint,
) : TaskRepository {

    override suspend fun populateTasksByProject(projectId: Int): Result<String, String> {
        try {
            val tasksActive = taskEndPoint.getProjectTasks(projectId, "Open").listTaskDto
            val tasksClosed = taskEndPoint.getProjectTasks(projectId, "Open").listTaskDto
            tasksActive?.toListEntities()?.let {
                //Log.d("toListEntities", it.toString())
                taskDao.insertTasks(it)
                if (tasksClosed != null) {
                    taskDao.insertTasks(tasksClosed.toListEntities())
                }
                return Success("Task for project are populated")
            }
            return Failure("Something wrong with network or server")
        } catch (e: Exception) {
            Log.e("TaskRepository", "tried to populate tasks but got an exception ${e.message}")
            return Failure("Something wrong with network or server")
        }
    }

    override fun getTasksByProject(projectId: Int): Flow<List<Task>> {
        return taskDao.getTasksWithProject(projectId).transform {
            Log.d("getTasksByProject",it.toString())
            emit(it.fromListTaskEntitiesToListTasks()) }
    }

    override fun getTaskById(taskId: Int): Flow<Task?> {
        //todo only download one task
        //Log.d("TaskRepository", "getTaskById" + taskId.toString())
        return taskDao.getTaskFromById(taskId).transform { emit(it.fromTaskEntityToTask()) }
    }

    override suspend fun populateTasks(): Result<String, String> {
        return networkCaller(
            call = { projectEndPoint.getAllProjects().listProjectDtos },
            onSuccess = { projects -> insertTasksToDbWithListProjects(projects?.toListEntities()) },
            onSuccessString = "Task created successfully",
            onFailureString = "Having problem while creating the task, please check the network connection"
        )
    }


    private fun insertTasksToDbWithListProjects(listProjects: List<ProjectEntity>?) {
        val listTasks = mutableListOf<TaskDto>()

        if (listProjects != null) {
            for (project in listProjects) {
                CoroutineScope(IO).launch {
                    val tasksActiveFromServer = taskEndPoint.getProjectTasks(project.id, "Open")
                    val tasksClosedFromServer =
                        taskEndPoint.getProjectTasks(project.id, "Closed")
                    tasksActiveFromServer.listTaskDto?.let { list ->
                        listTasks.addAll(list)
                        taskDao.insertTasks(list.toListEntities())
                    }
                    tasksClosedFromServer.listTaskDto?.let { list ->
                        listTasks.addAll(list)
                        taskDao.insertTasks(list.toListEntities())
                    }

                    taskDao.getAll().forEach { task ->
                        if (!listTasks.toListTaskIds().contains(task.id)) {
                            Log.d("listTasks", task.id.toString())
                            taskDao.deleteTask(task.id)
                        }
                    }
                }
            }
        }
    }


    override fun getAllUserTasks(): Flow<List<Task>> {
        return taskDao.getAllFlow().transform { emit( it.fromListTaskEntitiesToListTasks()) }
    }

    override suspend fun createTask(milestoneId: Int, task: Task): Result<String, String> {
        return networkCaller(
            call = { taskEndPoint.addTaskToProject(task.id, task.fromTaskEntityToPost(milestoneId)) },
            onSuccess = { populateTasks() },
            onSuccessString = "Task created successfully",
            onFailureString = "Having problem while creating the task, please check the network connection"
        )
    }

    override suspend fun createSubtask(
        subtask: Subtask
    ): Result<String, String> {
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
            onSuccessString = "Subtask created successfully",
            onFailureString = "Having problem while creating the subtask, please check the network connection"
        )
    }


    override suspend fun updateTask(
        taskId: Int,
        task: Task,
        taskStatus: String
    ): Result<String, String> {
        return networkCaller(
            call = { taskEndPoint.updateTask(taskId, task.fromTaskEntityToPost()) },
            onSuccess = {
                populateTasks()
                taskEndPoint.updateTaskStatus(taskId, TaskStatusPost(taskStatus, 1))
            },
            onSuccessString = "Task updated successfully",
            onFailureString = "Having problem while updating the task, please check the network connection"
        )
    }

    override suspend fun updateTaskStatus(taskId: Int, taskStatus: String) {
        Log.d("TaskRepository", "updating task status with id ${taskId}")
        Log.d("TaskRepository", "updating task status with status ${taskStatus}")
        taskEndPoint.updateTaskStatus(taskId, TaskStatusPost(taskStatus, 1))
        taskEndPoint.getTaskById(taskId).taskDto?.toEntity().let {
            taskDao.insertTasks(listOf(it!!))
        }
    }


    override suspend fun deleteTask(taskId: Int): Result<String, String> {
        return networkCaller(
            call = { taskEndPoint.deleteTask(taskId) },
            onSuccess = { populateTasks() },
            onSuccessString = "Task deleted successfully",
            onFailureString = "Having problem while deleting the task, please check the network connection"
        )
    }

}