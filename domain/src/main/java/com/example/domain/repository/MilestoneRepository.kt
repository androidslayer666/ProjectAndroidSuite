package com.example.domain.repository

import android.util.Log
import com.example.database.entities.MilestoneEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMilestonePost
import com.example.domain.model.Milestone
import com.example.network.dto.MilestonePost
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

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