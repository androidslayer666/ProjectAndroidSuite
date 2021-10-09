package com.example.domain.repository

import android.util.Log
import com.example.database.entities.ProjectEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toListUserEntity
import com.example.domain.mappers.toProjectEntity
import com.example.network.dto.ProjectDto
import com.example.network.dto.ProjectPost
import com.example.network.dto.ProjectStatusPost
import com.example.network.dto.ProjectTeamPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface ProjectRepository {
    suspend fun getProjects(): Result<String, String>



    fun getProjectFromDbById(projectId: Int): Flow<ProjectEntity?>

    fun getAllStoredProjects(): Flow<List<ProjectEntity>>

    fun projectsFromDb(): Flow<List<ProjectEntity>>

    suspend fun updateProject(
        projectId: Int,
        project: ProjectPost,
        projectStatus: String
    ): Result<String, String>

    suspend fun createProject(project: ProjectPost): Result<String, String>

    suspend fun deleteProject(projectId: Int): Result<String, String>



}