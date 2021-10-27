package com.example.data.endpoints

import com.example.domain.dto.MilestoneDto
import com.example.domain.dto.MilestonePost
import com.example.domain.dto.MilestonesTransporter
import retrofit2.http.*

interface MilestoneEndPoint {

    @GET("api/2.0/project/{id}/milestone")
    suspend fun getLateMilestonesByProjectId(@Path("id") projectId: Int): MilestonesTransporter

    @PUT("api/2.0/project/milestone/{milestoneId}")
    suspend fun updateMilestone(@Path("milestoneId") milestoneId: Int, @Body milestone: MilestonePost): MilestoneDto

    @POST("api/2.0/project/{projectId}/milestone")
    suspend fun putMilestoneToProject(@Path("projectId") projectId: Int, @Body milestone: MilestonePost): MilestoneDto

    @DELETE("api/2.0/project/milestone/{milestoneId}")
    suspend fun deleteMilestone(@Path("milestoneId")milestoneId: Int): MilestoneDto

}