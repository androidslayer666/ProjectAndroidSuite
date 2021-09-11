package com.example.domain.repository

import android.util.Log
import com.example.database.dao.MilestoneDao
import com.example.database.dao.ProjectDao
import com.example.database.dao.TaskDao
import com.example.database.entities.*
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.network.dto.TaskDto
import com.example.network.dto.TaskPost
import com.example.network.dto.TaskTransporter
import com.example.network.dto.auth.LoginResponse
import com.example.network.endpoints.MilestoneEndPoint
import com.example.network.endpoints.ProjectEndPoint
import com.example.network.endpoints.TaskEndPoint
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val milestoneDao: MilestoneDao,
    private val milestoneEndPoint: MilestoneEndPoint,
    private val taskEndPoint: TaskEndPoint,
    private val projectDao: ProjectDao,
    private val projectEndPoint: ProjectEndPoint,

    ) {

    suspend fun populateTasksByProject(projectId: Int) {
        val tasks = taskEndPoint.getProjectTasks(projectId).listTaskDto
        //val task = taskEndPoint.getTaskById(3509586)
//        Log.d("toListEntities", tasks.toString())
//        Log.d("toListEntities", tasks.toString())
        tasks?.toListEntities()?.let {
            //Log.d("toListEntities", it.toString())
            taskDao.insertTasks(it)
        }
    }

    fun getTasksByProject(projectId: Int): Flow<List<TaskEntity>> {
        return taskDao.getTasksWithProject(projectId)
    }

    fun getTaskById(taskId: Int): Flow<TaskEntity?> {
        Log.d("TaskRepository", "getTaskById" + taskId.toString())
        return taskDao.getTaskFromById(taskId)
    }

    suspend fun populateTasks() {
        Log.d("TaskRepository", "Starting populate tasks")
        val projects = projectEndPoint.getAllProjects().listProjectDtos
        Log.d("TaskRepository", "Get projects from server" + projects.toString())
        val listOfTasks = mutableListOf<TaskDto>()
        if (projects != null) {
            for(project in projects){
                val tasksFromServer = taskEndPoint.getProjectTasks(project.id)
                tasksFromServer.listTaskDto?.let {
                    Log.d("TaskRepository", "Adding tasks to list" + tasksFromServer.listTaskDto.toString())
                    listOfTasks.addAll(it)
                }
            }
        }
        // Better logic with comparison can be implemented to not redraw the ui
        taskDao.deleteAllTasks()
        Log.d("TaskRepository", "Inserting tasks" + listOfTasks.toString())
        taskDao.insertTasks(listOfTasks.toListEntities())
    }

    fun getAllUserTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAll()
    }

    suspend fun createTask(chosenProject: Int, task: TaskPost) {
        try{
            Log.d("createTask", "creating a task   $task")
            val reply = taskEndPoint.addTaskToProject(chosenProject, task)
            Log.d("createTask", "received a message after creating a task  $reply")
            //Cause we don't receive id from the server reply (somehow it's 0), we need to populate the db from scratch
            populateTasks()
//            if(reply != null) {
//                taskDao.insertTasks()
//            }
        }
        catch (e :java.lang.Exception){
            Log.d("TaskRepository", "tried to create a task but got an exception ${e.message}")
        }
    }

    suspend fun deleteTask(task: TaskEntity) : Result<String, Exception> {
        Log.d("TaskRepository", "Start deleting the task")
        val response = taskEndPoint.deleteTask(task.id)
        if (response.taskDto != null) {
            Log.d("TaskRepository", "Task deleted")
            populateTasks()
        } else {
            Log.d("TaskRepository", "Failed to delete the task")
        }
        return if (response.taskDto != null) {
            Success("The task successfully created")
        }else {
            Failure(Exception( "Unable to delete the task"))
        }
    }
}