package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.CommentEntity
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {

    @Query("SELECT * FROM milestones WHERE projectId = :projectId")
    suspend fun getMilestoneByProjectId(projectId: Int): List<MilestoneEntity>

    @Query("SELECT * FROM milestones WHERE projectId = :projectId")
    fun getMilestoneByProjectIdFlow(projectId: Int): Flow<List<MilestoneEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(taskList: List<MilestoneEntity>)

    @Query("DELETE FROM milestones WHERE id = :milestoneId")
    fun deleteMilestone(milestoneId: Int)

}