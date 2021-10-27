package com.example.data.endpoints

import com.example.domain.dto.FilesTransporter
import com.example.domain.dto.FilesTransporterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FileEndPoint {

    @GET("api/2.0/project/task/{taskId}/files")
    suspend fun getTaskFiles(@Path("taskId")taskId: Int): FilesTransporterResponse

    @GET("api/2.0/project/{id}/files")
    suspend fun getProjectFiles(@Path("id")projectId: Int): FilesTransporter


}