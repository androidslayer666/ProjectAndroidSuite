package com.example.network.endpoints

import com.example.network.dto.SubtaskDto
import com.example.network.dto.SubtaskPost
import com.example.network.dto.TasksTransporter
import retrofit2.http.*

interface SubtaskEndPoint {

    @GET("api/2.0/project/task/@self")
    suspend fun getUserTask(): TasksTransporter

    @POST("api/2.0/project/task/{taskid}")
    suspend fun addSubtaskToTask(@Path("taskid")taskId: Int, @Body subtask: SubtaskPost) : SubtaskDto

    @PUT("api/2.0/project/task/{taskid}/{subtaskid}")
    suspend fun updateSubtask(@Path("taskid") taskId: Int, @Path("subtaskid") subtaskId: Int): SubtaskDto

    @DELETE("api/2.0/project/task/{taskid}/{subtaskid}")
    suspend fun deleteSubtask(@Path("taskid") taskId: Int, @Path("subtaskid") subtaskId: Int): SubtaskDto

}
