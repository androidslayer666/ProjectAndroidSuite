package com.example.domain.interactor.project

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateProject(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(project: Project): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getProjects()
        }
        return projectRepository.createProject(project)
    }

}