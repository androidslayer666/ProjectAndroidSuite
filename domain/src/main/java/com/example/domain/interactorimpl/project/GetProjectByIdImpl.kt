package com.example.domain.interactorimpl.project

import com.example.domain.interactor.project.GetProjectById
import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetProjectByIdImpl(
    private val projectRepository: ProjectRepository
) : GetProjectById {
    override operator fun invoke (projectId: Int?): Flow<Project?> {
        return projectRepository.getProjectFromDbById(projectId?:0)
    }
}