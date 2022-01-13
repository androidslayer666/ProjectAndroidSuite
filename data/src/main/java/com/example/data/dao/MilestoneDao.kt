package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.MilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {

    @Query("SELECT * FROM milestones WHERE projectId = :projectId")
    suspend fun getMilestoneByProjectId(projectId: Int): List<MilestoneEntity>

    @Query("SELECT * FROM milestones WHERE projectId = :projectId")
    fun getMilestonesByProjectIdFlow(projectId: Int): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE id = :milestoneId")
    suspend fun getMilestoneById(milestoneId: Int): MilestoneEntity

    @Query("SELECT * FROM milestones WHERE id = :milestoneId")
    fun getMilestoneByIdFlow(milestoneId: Int): Flow<MilestoneEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(taskList: List<MilestoneEntity>)

    @Query("DELETE FROM milestones WHERE id = :milestoneId")
    fun deleteMilestone(milestoneId: Int)

    @Query("SELECT * FROM milestones")
    fun getAllMilestones(): Flow<List<MilestoneEntity>>

    @Query("DELETE FROM milestones")
    suspend fun clearLocalCache()

}