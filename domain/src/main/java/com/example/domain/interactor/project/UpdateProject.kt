package com.example.domain.interactor.project

import com.example.domain.filters.project.ProjectStatus
import com.example.domain.model.Project
import com.example.domain.utils.Result

interface UpdateProject {
    suspend operator fun invoke(
        projectId: Int,
        project: Project,
        projectStatus: ProjectStatus?
    ): Result<String, String>
}