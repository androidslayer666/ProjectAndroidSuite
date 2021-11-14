package com.example.domain.interactorimpl.project

import com.example.domain.interactor.project.CreateProject
import com.example.domain.model.Project
import com.example.domain.repository.ProjectRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateProjectImpl(
    private val projectRepository: ProjectRepository
) : CreateProject {
    override suspend operator fun invoke(project: Project): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getProjects()
        }
        return projectRepository.createProject(project)
    }

}