package com.example.network.endpoints

import com.example.network.dto.TaskDto
import com.example.network.dto.TaskPost
import com.example.network.dto.TaskTransporter
import com.example.network.dto.TasksTransporter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TaskEndPoint {

    @GET("api/2.0/project/task/@self")
    suspend fun getUserTasks(): TasksTransporter

    @GET("api/2.0/project/task/{taskid}")
    suspend fun getTaskById(@Path("taskid")taskId: Int): TaskTransporter

    @GET("api/2.0/project/{projectid}/task")
    suspend fun getProjectTasks(@Path("projectid") projectId: Int): TasksTransporter

    @POST("api/2.0/project/{projectid}/task")
    suspend fun addTaskToProject(@Path("projectid") projectId: Int, @Body task: TaskPost): TaskDto

    @PUT("api/2.0/project/task/{taskid}")
    suspend fun updateTask(@Path("taskid") taskId: Int): TaskTransporter

    @DELETE("api/2.0/project/task/{taskid}")
    suspend fun deleteTask(@Path("taskid") taskId: Int): TaskTransporter

}
