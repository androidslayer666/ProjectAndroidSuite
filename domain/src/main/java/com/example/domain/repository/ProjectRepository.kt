package com.example.domain.repository

import com.example.domain.Result
import com.example.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getProjects(): Result<String, String>

    fun getProjectFromDbById(projectId: Int): Flow<Project?>

    fun getAllStoredProjects(): Flow<List<Project>>

    suspend fun updateProject(
        projectId: Int,
        project: Project,
        projectStatus: String
    ): Result<String, String>

    suspend fun createProject(project: Project): Result<String, String>

    suspend fun deleteProject(projectId: Int): Result<String, String>



}