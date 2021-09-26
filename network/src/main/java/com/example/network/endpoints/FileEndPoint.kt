package com.example.network.endpoints

import com.example.network.dto.FilesSubTransporter
import com.example.network.dto.FilesTransporter
import com.example.network.dto.FilesTransporterResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface FileEndPoint {

    @GET("api/2.0/project/task/{taskId}/files")
    suspend fun getTaskFiles(@Path("taskId")taskId: Int): FilesTransporterResponse

    @GET("api/2.0/project/{id}/files")
    suspend fun getProjectFiles(@Path("id")projectId: Int): FilesTransporter


}