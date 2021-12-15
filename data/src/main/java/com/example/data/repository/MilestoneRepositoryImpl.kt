package com.example.data.repository

import android.util.Log
import com.example.data.dao.MilestoneDao
import com.example.data.endpoints.MilestoneEndPoint
import com.example.domain.mappers.*
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import com.example.domain.utils.Result
import com.example.domain.utils.networkCaller
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
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
        return milestoneDao.getMilestonesByProjectIdFlow(projectId)
            .transform {
                emit(it.fromListMilestoneEntitiesToListMilestone())
            }
    }

    override suspend fun getMilestoneById(milestoneId: Int): Milestone? {
        Log.d("MilestoneRepository", milestoneId.toString())
        return milestoneDao.getMilestoneById(milestoneId)?.fromMilestoneEntityToMilestone()
    }

    override fun getMilestoneByIdFlow(milestoneId: Int): Flow<Milestone?> {
        return milestoneDao.getMilestoneByIdFlow(milestoneId).transform {
            it?.fromMilestoneEntityToMilestone()
        }
    }

    override fun getMilestones(): Flow<List<Milestone>> {
        return milestoneDao.getAllMilestones()
            .transform {
                emit(it.fromListMilestoneEntitiesToListMilestone())
            }
    }

    override suspend fun putMilestoneToProject(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String> {
        Log.d("MilestoneRepository", "Started creating milestone  ${milestone.toMilestonePost()}")

        return networkCaller(
            call = {
                milestoneEndPoint.putMilestoneToProject(
                    projectId,
                    milestone.toMilestonePost()
                )
            },
            onSuccess = { populateMilestonesByProject(projectId) },
            onSuccessString = "Milestone added successfully",
            onFailureString = "Having problem while creating the milestone, please check the network connection"
        )
    }

    override suspend fun deleteMilestone(
        milestoneId: Int,
        projectId: Int?
    ): Result<String, String> {

        return networkCaller(
            call = { milestoneEndPoint.deleteMilestone(milestoneId) },
            onSuccess = {
                if (projectId != null) {
                    populateMilestonesByProject(projectId)
                }
            },
            onSuccessString = "Milestone deleted successfully",
            onFailureString = "Having problem while deleting the milestone, please check the network connection"
        )
    }

    override suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String> {
        Log.d(
            "MilestoneRepository",
            "Started creating with ${milestone.id}  milestone  ${milestone.toMilestonePost()}"
        )
        return networkCaller(
            call = { milestoneEndPoint.updateMilestone(milestone.id, milestone.toMilestonePost()) },
            onSuccess = { populateMilestonesByProject(projectId) },
            onSuccessString = "Milestone updated successfully",
            onFailureString = "Having problem while updating the milestone, please check the network connection"
        )
    }
}