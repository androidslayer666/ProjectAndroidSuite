package com.example.data.repository

import android.util.Log
import com.example.data.ResponseIsEmptyException
import com.example.data.dao.MilestoneDao
import com.example.data.endpoints.MilestoneEndPoint
import com.example.domain.dto.MessageDto
import com.example.domain.dto.MilestoneDto
import com.example.domain.mappers.*
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
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

    override suspend fun populateMilestonesByProject(projectId: Int) : Result<String, Throwable>{
        try {
            val milestones =
                milestoneEndPoint.getLateMilestonesByProjectId(projectId).listMilestoneDtos

            return if (milestones == null) {
                Failure(ResponseIsEmptyException())
            }else {
                deleteMilestoneFromDbIfDeletedOnServer(projectId, milestones)
                milestoneDao.insertMilestones( milestones.toListEntities(projectId))
                Success("")
            }

            //Log.d("MilestoneRepository", milestones.toString())
        } catch (e: Exception) {
            return Failure(e)
        }
    }

    private suspend fun deleteMilestoneFromDbIfDeletedOnServer(
        projectId : Int,
        milestones: List<MilestoneDto>?
    ){
        //val messagesFromDb = messageDao.getMessageByProjectId(projectId)
        milestoneDao.getMilestoneByProjectId(projectId).forEach {
            if (milestones?.toListMilestoneIds()?.contains(it.id) != true) {
                milestoneDao.deleteMilestone(it.id)
            }
        }
    }


    override fun getMilestonesByProjectFlow(projectId: Int): Flow<List<Milestone>> {
        return milestoneDao.getMilestonesByProjectIdFlow(projectId)
            .transform {
                emit(it.fromListMilestoneEntitiesToListMilestone())
            }
    }

    override suspend fun getMilestoneById(milestoneId: Int): Milestone? {
        return milestoneDao.getMilestoneById(milestoneId)?.fromMilestoneEntityToMilestone()
    }

    override fun getMilestoneByIdFlow(milestoneId: Int): Flow<Milestone?> {
        return milestoneDao.getMilestoneByIdFlow(milestoneId).transform {
            emit(it?.fromMilestoneEntityToMilestone())
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
    ): Result<String, Throwable> {

        return networkCaller(
            call = {
                milestoneEndPoint.putMilestoneToProject(
                    projectId,
                    milestone.toMilestonePost()
                )
            },
            onSuccess = { populateMilestonesByProject(projectId) }
        )
    }

    override suspend fun deleteMilestone(
        milestoneId: Int,
        projectId: Int?
    ): Result<String, Throwable> {

        return networkCaller(
            call = { milestoneEndPoint.deleteMilestone(milestoneId) },
            onSuccess = {
                if (projectId != null) {
                    populateMilestonesByProject(projectId)
                }
            }
        )
    }

    override suspend fun updateMilestoneToProject(
        projectId: Int,
        milestone: Milestone
    ): Result<String, Throwable> {

        return networkCaller(
            call = { milestoneEndPoint.updateMilestone(milestone.id, milestone.toMilestonePost()) },
            onSuccess = { populateMilestonesByProject(projectId) }
        )
    }

    override suspend fun clearLocalCache() {
        milestoneDao.clearLocalCache()
    }
}