package com.example.domain.repository

import android.util.Log
import com.example.database.dao.MilestoneDao
import com.example.database.entities.MilestoneEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMilestonePost
import com.example.network.dto.MilestonePost
import com.example.network.endpoints.MilestoneEndPoint
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilestoneRepository @Inject constructor(

    private val milestoneEndPoint: MilestoneEndPoint,
    private val milestoneDao: MilestoneDao
) {

    suspend fun populateMilestonesByProject(projectId: Int) {
        try {
            val milestones =
                milestoneEndPoint.getLateMilestonesByProjectId(projectId).listMilestoneDtos
            milestones?.toListEntities(projectId)?.let {
                milestoneDao.insertMilestones(it)
            }
            milestoneDao.getMilestoneByProjectId(projectId).forEach {
                if (milestones?.toListMilestoneIds()?.contains(it.id) != true) {
                    milestoneDao.deleteMilestone(it.id)
                }
            }
            //Log.d("MilestoneRepository", milestones.toString())
        } catch (e: Exception) {
            Log.d("MilestoneRepository", "caught an exception : $e")
        }
    }

    fun getMilestonesByProjectFlow(projectId: Int): Flow<List<MilestoneEntity>> {
        return milestoneDao.getMilestonesByProjectIdFlow(projectId)
    }

    suspend fun getMilestoneById(milestoneId: Int): MilestoneEntity {
        return milestoneDao.getMilestoneById(milestoneId)
    }


    suspend fun putMilestoneToProject(
        projectId: Int,
        milestone: MilestonePost
    ): Result<String, String> {
        Log.d("MilestoneRepository", "Started creating milestone  ${milestone}")

        return networkCaller(
            call = { milestoneEndPoint.putMilestoneToProject(projectId, milestone) },
            onSuccess = { populateMilestonesByProject(projectId) },
            onSuccessString = "Milestone added successfully",
            onFailureString = "Having problem while creating the milestone, please check the network connection"
        )
    }

    suspend fun deleteMilestone(milestoneId: Int, projectId: Int?): Result<String, String> {

        return networkCaller(
            call = { milestoneEndPoint.deleteMilestone(milestoneId) },
            onSuccess = {
                if (projectId != null) {
                    populateMilestonesByProject(projectId)
                }
            },
            onSuccessString = "Milestone deleted successfully",
            onFailureString = "Having problem while creating the milestone, please check the network connection"
        )
    }

    suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: MilestoneEntity
    ): Result<String, String> {
        Log.d("MilestoneRepository", "Started creating with ${milestone.id}  milestone  ${milestone.toMilestonePost()}")
        return networkCaller(
            call = { milestoneEndPoint.updateMilestone(milestone.id, milestone.toMilestonePost()) },
            onSuccess = { populateMilestonesByProject(projectId) },
            onSuccessString = "Milestone updated successfully",
            onFailureString = "Having problem while updating the milestone, please check the network connection"
        )
    }
}