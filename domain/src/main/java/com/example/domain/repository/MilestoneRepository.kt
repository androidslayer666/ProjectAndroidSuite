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
import com.example.network.dto.MilestonesTransporter
import com.example.network.dto.TaskDto
import com.example.network.dto.TaskTransporter
import com.example.network.endpoints.MilestoneEndPoint
import com.example.network.endpoints.TaskEndPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilestoneRepository @Inject constructor(

    private val milestoneEndPoint: MilestoneEndPoint,
    private val milestoneDao: MilestoneDao
) {

    suspend fun populateMilestonesByProject(projectId: Int) {
        val milestones = milestoneEndPoint.getLateMilestonesByProjectId(projectId)
        milestones.listMilestoneDtos?.toListEntities(projectId)?.let {
            milestoneDao.insertMilestones(it)
        }
        Log.d("TaskRepository", milestones.toString())
    }

    fun getMilestoneByProject(projectId: Int): Flow<List<MilestoneEntity>> {
        return milestoneDao.getMilestoneByProjectId(projectId)
    }
}