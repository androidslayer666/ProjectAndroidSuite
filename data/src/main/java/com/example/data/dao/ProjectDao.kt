package com.example.data.dao

import androidx.room.*
import com.example.domain.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects")
    fun getAllFlow(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects")
    suspend fun getAll(): List<ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projectList: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun getProjectFromDbById(projectId: Int) : Flow<ProjectEntity?>

    @Query("DELETE FROM projects WHERE id = :projectId")
    fun deleteProject(projectId: Int)

    @Query("DELETE FROM projects")
    fun deleteAllProjects()

    @Update
    fun updateProject(project: ProjectEntity)
}