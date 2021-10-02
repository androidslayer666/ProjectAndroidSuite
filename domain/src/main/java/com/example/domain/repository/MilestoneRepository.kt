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
        Log.d("ProjectRepository", "Started creating milestone  ${milestone}")
        try {
            val response =
                milestoneEndPoint.putMilestoneToProject(projectId, milestone)

            Log.d("ProjectRepository", response.toString())
            if (response != null) {
                Log.d("ProjectRepository", "Project created")
                //because response from server doesn't give id
                populateMilestonesByProject(projectId)
                return Success("Project successfully created")
            } else {
                return Failure("Project was not created due to a server problem")
            }
        } catch (e: Exception) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Project was not created, please check network or ask the developer to fix this")
        }
    }

    suspend fun deleteMilestone(milestoneId: Int, projectId: Int?): Result<String, String> {
        try {
            val response = milestoneEndPoint.deleteMilestone(milestoneId)
            if (response != null) {
                if (projectId != null) {
                    populateMilestonesByProject(projectId)
                }
                return Success("Milestone successfully deleted")
            } else {
                return Failure("Milestone was not deleted due to a server problem")
            }
        } catch (e: Exception) {
            Log.d(
                "MilestoneRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Milestone was not deleted, please check network or ask the developer to fix this")
        }
    }

    suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: MilestoneEntity
    ): Result<String, String> {
        Log.d("ProjectRepository", "Started creating with ${milestone.id}  milestone  ${milestone.toMilestonePost()}")
        try {
            val response =
                milestoneEndPoint.updateMilestone(milestone.id, milestone.toMilestonePost())

            Log.d("ProjectRepository", response.toString())
            if (response != null) {
                Log.d("ProjectRepository", "Project created")
                //because response from server doesn't give id
                populateMilestonesByProject(projectId)
                return Success("Project successfully created")
            } else {
                return Failure("Project was not created due to a server problem")
            }
        } catch (e: Exception) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Project was not created, please check network or ask the developer to fix this")
        }
    }

}