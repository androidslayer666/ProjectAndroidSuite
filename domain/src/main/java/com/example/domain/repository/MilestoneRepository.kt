package com.example.domain.repository

import android.util.Log
import com.example.database.dao.MilestoneDao
import com.example.database.dao.TaskDao
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.database.entities.TaskOrMilestoneEntity
import com.example.database.entities.UniversalEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMilestonePost
import com.example.network.dto.MilestonesTransporter
import com.example.network.dto.TaskDto
import com.example.network.dto.TaskTransporter
import com.example.network.endpoints.MilestoneEndPoint
import com.example.network.endpoints.TaskEndPoint
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
        val milestones = milestoneEndPoint.getLateMilestonesByProjectId(projectId)
        milestones.listMilestoneDtos?.toListEntities(projectId)?.let {
            milestoneDao.insertMilestones(it)
        }
        //Log.d("MilestoneRepository", milestones.toString())
        }catch (e: Exception){
            Log.d("MilestoneRepository", "caught an exception : $e")
        }
    }

    fun getMilestoneByProject(projectId: Int): Flow<List<MilestoneEntity>> {
        return milestoneDao.getMilestoneByProjectId(projectId)
    }


    suspend fun putMilestoneToProject(projectId : Int, milestone: MilestoneEntity) : Result<String, String>{
        Log.d("ProjectRepository", "Started creating milestone  $milestone")
        try {
            val response = milestoneEndPoint.putMilestoneToProject(projectId, milestone.toMilestonePost())

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