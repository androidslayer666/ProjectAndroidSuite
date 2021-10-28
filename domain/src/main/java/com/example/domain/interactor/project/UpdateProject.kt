package com.example.domain.interactor.project

import com.example.domain.utils.ProjectStatus
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateProject(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        project: Project,
        projectStatus: ProjectStatus?
    ): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getProjects()
        }

        return projectRepository.updateProject(
            projectId,
            project,
            when (projectStatus) {
                ProjectStatus.ACTIVE -> "open"
                ProjectStatus.PAUSED -> "Paused"
                ProjectStatus.STOPPED -> "Closed"
                else -> "open"
            }
        )
    }

}