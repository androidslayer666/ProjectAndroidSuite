package com.example.domain.repository

import com.example.domain.Result
import com.example.domain.model.Milestone
import kotlinx.coroutines.flow.Flow

interface MilestoneRepository {

    suspend fun populateMilestonesByProject(projectId: Int)

    fun getMilestonesByProjectFlow(projectId: Int): Flow<List<Milestone>>

    suspend fun getMilestoneById(milestoneId: Int): Milestone


    suspend fun putMilestoneToProject(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String>

    suspend fun deleteMilestone(milestoneId: Int, projectId: Int?): Result<String, String>

    suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String>
}