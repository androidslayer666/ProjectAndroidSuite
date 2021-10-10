package com.example.domain.repositoryimpl

import android.util.Log
import com.example.database.dao.MilestoneDao
import com.example.database.entities.MilestoneEntity
import com.example.domain.mappers.fromListMilestoneEntitiesToListMilestone
import com.example.domain.mappers.fromMilestoneEntityToMilestone
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMilestonePost
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.Result
import com.example.domain.repository.networkCaller
import com.example.domain.repository.toListMilestoneIds
import com.example.network.dto.MilestonePost
import com.example.network.endpoints.MilestoneEndPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilestoneRepositoryImpl @Inject constructor(
    private val milestoneEndPoint: MilestoneEndPoint,
    private val milestoneDao: MilestoneDao
) : MilestoneRepository {

    override suspend fun populateMilestonesByProject(projectId: Int) {
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

    override fun getMilestonesByProjectFlow(projectId: Int): Flow<List<Milestone>> {
        return milestoneDao.getMilestonesByProjectIdFlow(projectId).transform { emit(it.fromListMilestoneEntitiesToListMilestone()) }
    }

    override suspend fun getMilestoneById(milestoneId: Int): Milestone {
        return milestoneDao.getMilestoneById(milestoneId).fromMilestoneEntityToMilestone()
    }

    override suspend fun putMilestoneToProject(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String> {
        Log.d("MilestoneRepository", "Started creating milestone  ${milestone.toMilestonePost()}")

        return networkCaller(
            call = { milestoneEndPoint.putMilestoneToProject(projectId, milestone.toMilestonePost()) },
            onSuccess = { populateMilestonesByProject(projectId) },
            onSuccessString = "Milestone added successfully",
            onFailureString = "Having problem while creating the milestone, please check the network connection"
        )
    }

    override suspend fun deleteMilestone(milestoneId: Int, projectId: Int?): Result<String, String> {

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

    override suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: Milestone
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