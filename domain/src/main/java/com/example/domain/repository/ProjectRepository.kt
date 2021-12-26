package com.example.domain.repository

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getProjects(): Result<String, Throwable>

    fun getProjectFromDbById(projectId: Int): Flow<Project?>

    fun getAllStoredProjects(): Flow<List<Project>>

    suspend fun updateProject(
        projectId: Int,
        project: Project,
        projectStatus: String
    ): Result<String, Throwable>

    suspend fun createProject(project: Project): Result<String, Throwable>

    suspend fun deleteProject(projectId: Int): Result<String, Throwable>



}