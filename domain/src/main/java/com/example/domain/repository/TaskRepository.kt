package com.example.domain.repository

import android.util.Log
import com.example.database.dao.TaskDao
import com.example.database.entities.TaskEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.network.dto.SubtaskPost
import com.example.network.dto.TaskDto
import com.example.network.dto.TaskPost
import com.example.network.dto.TaskStatusPost
import com.example.network.endpoints.ProjectEndPoint
import com.example.network.endpoints.TaskEndPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskEndPoint: TaskEndPoint,
    private val projectEndPoint: ProjectEndPoint,
    ) {

    suspend fun populateTasksByProject(projectId: Int): Result<String, String> {
        try {
            val tasksActive = taskEndPoint.getProjectTasks(projectId,"Open").listTaskDto
            val tasksClosed = taskEndPoint.getProjectTasks(projectId,"Open").listTaskDto
            tasksActive?.toListEntities()?.let {
                //Log.d("toListEntities", it.toString())
                taskDao.insertTasks(it)
                if(tasksClosed != null) {
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

    //todo can either go to net from
    private suspend fun attachMilestoneToTask(task: TaskEntity){

    }

    fun getTasksByProject(projectId: Int): Flow<List<TaskEntity>> {
        return taskDao.getTasksWithProject(projectId)
    }

    fun getTaskById(taskId: Int): Flow<TaskEntity?> {
        //Log.d("TaskRepository", "getTaskById" + taskId.toString())
        return taskDao.getTaskFromById(taskId)
    }

    suspend fun populateTasks(): Result<String, String> {
        try {
            //Log.d("TaskRepository", "Starting populate tasks")
            val projects = projectEndPoint.getAllProjects().listProjectDtos
            //Log.d("TaskRepository", "Get projects from server" + projects.toString())
            val listTasks = mutableListOf<TaskDto>()
            if (projects != null) {
                for (project in projects) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val tasksActiveFromServer = taskEndPoint.getProjectTasks(project.id, "Open")
                        val tasksClosedFromServer = taskEndPoint.getProjectTasks(project.id, "Closed")
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
                //Log.d("listTasks", listTasks.toString())


                return Success("Tasks are populated")
            } else {
                return Failure("Something wrong with network or server")
            }
        } catch (e: Exception) {
            Log.e("TaskRepository", "tried to populate tasks but got an exception ${e.message}")
            return Failure("Something wrong with network or server")
        }
    }

    fun getAllUserTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllFlow()
    }

    suspend fun createTask(chosenProject: Int, task: TaskPost): Result<String, String> {
        try {
            Log.d("createTask", "creating a task   $task")
            val reply = taskEndPoint.addTaskToProject(chosenProject, task)
            Log.d("createTask", "received a message after creating a task  $reply")
            //Cause we don't receive id from the server reply (somehow it's 0), we need to populate the db from scratch
            if (reply != null) {
                populateTasks()
                return Success("Project successfully updated")
            } else {
                return Failure("Error during updating the project")
            }
        } catch (e: java.lang.Exception) {
            Log.d("TaskRepository", "tried to create a task but got an exception ${e.message}")
            return Failure("Error during updating the project")
        }
    }

    suspend fun createSubtask(taskId: Int, subtask: SubtaskPost) : Result<String, String>{
        try {
            Log.d("createTask", "task id   $taskId")
            Log.d("createTask", "creating a subtask   $subtask")
            val reply = taskEndPoint.createSubtask(taskId, subtask)
            Log.d("createTask", "received a message after creating a task  $reply")
            //Cause we don't receive id from the server reply (somehow it's 0), we need to populate the db from scratch
            if (reply != null) {
                populateTasks()
                return Success("Subtask successfully created")
            } else {
                return Failure("Error during creating subtask")
            }
        } catch (e: java.lang.Exception) {
            Log.d("TaskRepository", "tried to create a subtask but got an exception ${e.message}")
            return Failure("Error during creating subtask")
        }
    }


    suspend fun updateTask(taskId: Int, task: TaskPost, taskStatus: String): Result<String, String> {
        try {
            Log.d("update Task", "update a task   $task")
            val reply = taskEndPoint.updateTask(taskId, task)
            Log.d("updateTask", "received a message after update a task  $reply")
            //Cause we don't receive id from the server reply (somehow it's 0), we need to populate the db from scratch

            if (reply != null) {
                taskEndPoint.updateTaskStatus(taskId, TaskStatusPost(taskStatus, 1))
                populateTasks()
                return Success("Task successfully updated")
            } else {
                return Failure("Error during updating the task")
            }
        } catch (e: java.lang.Exception) {
            Log.d("TaskRepository", "tried to create a task but got an exception ${e.message}")
            return Failure("Error during updating the task")
        }
    }

    suspend fun updateTaskStatus(taskId: Int, taskStatus: String) {
        Log.d("TaskRepository", "updating task status with id ${taskId}")
        Log.d("TaskRepository", "updating task status with status ${taskStatus}")
        taskEndPoint.updateTaskStatus(taskId, TaskStatusPost(taskStatus, 1))
        taskEndPoint.getTaskById(taskId).taskDto?.toEntity().let{
            taskDao.insertTasks(listOf(it!!))
        }
    }


    suspend fun deleteTask(taskId: Int): Result<String, String> {
        try {
            Log.d("TaskRepository", "Start deleting the task with id : $taskId")
            val response = taskEndPoint.deleteTask(taskId)
            if (response.taskDto != null) {
                Log.d("TaskRepository", "Task deleted")
                populateTasks()
                return Success("The task successfully deleted")
            } else {
                Log.d("TaskRepository", "Failed to delete the task")
                return Failure("Unable to delete the task")
            }
        } catch (e: Exception) {
            Log.d("TaskRepository", "tried to create a task but got an exception ${e.message}")
            return Failure("Unable to delete the task")
        }
    }
}