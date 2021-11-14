package com.example.domain.interactorimpl.project

import com.example.domain.interactor.project.DeleteProject
import com.example.domain.repository.ProjectRepository
import com.example.domain.utils.Result

class DeleteProjectImpl(
    private val projectRepository: ProjectRepository
) : DeleteProject {
    override suspend operator fun invoke(projectId: Int): Result<String, String> {
        return projectRepository.deleteProject(projectId)
    }
}