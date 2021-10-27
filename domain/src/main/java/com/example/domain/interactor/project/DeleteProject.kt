package com.example.domain.interactor.project

import com.example.domain.Result
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteProject(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Int): Result<String, String> {
        return projectRepository.deleteProject(projectId)
    }
}