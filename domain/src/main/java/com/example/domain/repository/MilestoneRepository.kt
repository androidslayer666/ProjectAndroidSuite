package com.example.domain.repository

import android.util.Log
import com.example.database.entities.MilestoneEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMilestonePost
import com.example.network.dto.MilestonePost
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface MilestoneRepository {

    suspend fun populateMilestonesByProject(projectId: Int)

    fun getMilestonesByProjectFlow(projectId: Int): Flow<List<MilestoneEntity>>

    suspend fun getMilestoneById(milestoneId: Int): MilestoneEntity


    suspend fun putMilestoneToProject(
        projectId: Int,
        milestone: MilestonePost
    ): Result<String, String>

    suspend fun deleteMilestone(milestoneId: Int, projectId: Int?): Result<String, String>

    suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: MilestoneEntity
    ): Result<String, String>
}