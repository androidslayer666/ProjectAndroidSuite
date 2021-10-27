package com.example.data.endpoints

import com.example.domain.dto.*
import retrofit2.http.*

interface ProjectEndPoint {

    @GET("api/2.0/project/{projectId}/files")
    suspend fun getProjectFiles(@Path("projectId")projectId: Int): FilesTransporter

    @GET("api/2.0/project/{id}")
    suspend fun getProjectById(@Path("id")projectId: Int): ProjectTransporter

    @GET("api/2.0/project")
    suspend fun getAllProjects(): ProjectsTransporter

    @GET("api/2.0/project/@self")
    suspend fun getUserProjects(): ProjectsTransporter

    @POST("api/2.0/project")
    suspend fun createProject(@Body projectPostDto: ProjectPost): ProjectDto

    @PUT("api/2.0/project/{projectId}/follow")
    suspend fun followOrUnfollowProject(@Path("projectId")projectId: Int)

    @PUT("api/2.0/project/{projectId}")
    suspend fun updateProject(@Path("projectId")projectId: Int,@Body projectPost: ProjectPost): ProjectTransporter

    @PUT("api/2.0/project/{id}/status")
    suspend fun updateProjectStatus(@Path("id")projectId: Int,@Body projectStatus: ProjectStatusPost): ProjectDto

    @PUT("api/2.0/project/{projectid}/team")
    suspend fun updateProjectTeam(@Path("projectid")projectId: Int,@Body projectStatus: ProjectTeamPost): ProjectDto

    @DELETE("api/2.0/project/{projectId}")
    suspend fun deleteProject(@Path("projectId")projectId: Int): ProjectTransporter

}