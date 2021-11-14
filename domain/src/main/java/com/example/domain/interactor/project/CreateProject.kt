package com.example.domain.interactor.project

import com.example.domain.model.Project
import com.example.domain.utils.Result

interface CreateProject {
    suspend operator fun invoke(project: Project): Result<String, String>
    }