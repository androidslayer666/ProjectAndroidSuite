package com.example.domain.interactorimpl.project

import com.example.domain.filters.project.ProjectStatus
import com.example.domain.interactor.project.UpdateProject
import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateProjectImpl(
    private val projectRepository: ProjectRepository
) : UpdateProject {
    override suspend operator fun invoke(
        projectId: Int,
        project: Project,
        projectStatus: ProjectStatus?
    ): Result<String, String> {
//        CoroutineScope(Dispatchers.IO).launch {
//            projectRepository.getProjects()
//        }

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