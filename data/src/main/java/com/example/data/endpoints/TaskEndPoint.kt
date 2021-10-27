package com.example.data.endpoints

import com.example.domain.dto.*
import retrofit2.http.*

interface TaskEndPoint {

    @GET("api/2.0/project/task/@self")
    suspend fun getUserTasks(): TasksTransporter

    @GET("api/2.0/project/task/@self/{status}")
    suspend fun getTasksByStatus(@Path("status")taskStatus: String): TasksTransporter

    @GET("api/2.0/project/task/{taskid}")
    suspend fun getTaskById(@Path("taskid")taskId: Int): TaskTransporter

    @GET("api/2.0/project/{projectid}/task/{status}")
    suspend fun getProjectTasks(@Path("projectid") projectId: Int, @Path("status") status: String): TasksTransporter

    @POST("api/2.0/project/{projectid}/task")
    suspend fun addTaskToProject(@Path("projectid") projectId: Int, @Body task: TaskPost): TaskDto?

    @PUT("api/2.0/project/task/{taskid}")
    suspend fun updateTask(@Path("taskid") taskId: Int, @Body task: TaskPost): TaskTransporter?

    @PUT("api/2.0/project/task/{taskid}/status")
    suspend fun updateTaskStatus(@Path("taskid") taskId: Int, @Body taskStatus: TaskStatusPost): TaskDto?


    @DELETE("api/2.0/project/task/{taskid}")
    suspend fun deleteTask(@Path("taskid") taskId: Int): TaskTransporter

    @POST("api/2.0/project/task/{taskid}")
    suspend fun createSubtask(@Path("taskid") taskId: Int, @Body subtask: SubtaskPost)

}
