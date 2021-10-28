package com.example.domain.interactor.project

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.repository.ProjectRepository

class DeleteProject(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Int): Result<String, String> {
        return projectRepository.deleteProject(projectId)
    }
}