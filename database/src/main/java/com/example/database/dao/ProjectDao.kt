package com.example.database.dao

import androidx.room.*
import com.example.database.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects")
    fun getAll(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projectList: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectFromDbById(projectId: Int) : ProjectEntity

    @Delete
    fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM projects")
    fun deleteAllProjects()

    @Update
    fun updateProject(project: ProjectEntity)
}