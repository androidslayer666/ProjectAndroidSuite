package com.example.network.endpoints

import com.example.network.Constants
import com.example.network.Constants.GET_PROJECTS_URL
import com.example.network.authTokenInterceptor
import com.example.network.dto.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProjectEndPoint {

    @GET(GET_PROJECTS_URL)
    fun getParticipatedProjects(): Call<ProjectsTransporter>

    @GET("api/2.0/project/{projectId}/files")
    suspend fun getProjectFiles(@Path("projectId")projectId: Int): FilesTransporter

    @GET("api/2.0/project/{id}")
    suspend fun getProjectById(@Path("id")projectId: Int): ProjectTransporter

    @GET("api/2.0/project")
    suspend fun getAllProjects(): ProjectsTransporter

    @GET("api/2.0/project/@follow")
    suspend fun getFollowedProjects(): ProjectsTransporter

    @POST("api/2.0/project")
    suspend fun createProject(@Body projectPostDto: ProjectPost): ProjectDto

    @PUT("api/2.0/project/{projectId}/follow")
    suspend fun followOrUnfollowProject(@Path("projectId")projectId: Int)

    @PUT("api/2.0/project/{projectId}")
    suspend fun updateProject(@Path("projectId")projectId: Int,@Body projectPost: ProjectPost): ProjectTransporter

    @PUT("api/2.0/project/{id}/status")
    suspend fun updateProjectStatus(@Path("id")projectId: Int,@Body projectStatus: ProjectStatusPost): ProjectDto

    @DELETE("api/2.0/project/{projectId}")
    suspend fun deleteProject(@Path("projectId")projectId: Int): ProjectTransporter

}