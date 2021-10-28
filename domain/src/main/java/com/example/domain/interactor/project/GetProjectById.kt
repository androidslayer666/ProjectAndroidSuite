package com.example.domain.interactor.project

import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetProjectById(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke (projectId: Int?): Flow<Project?> {
        return projectRepository.getProjectFromDbById(projectId?:0)
    }
}