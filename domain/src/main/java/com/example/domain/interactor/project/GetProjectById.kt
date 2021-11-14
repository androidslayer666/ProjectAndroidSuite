package com.example.domain.interactor.project

import com.example.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface GetProjectById {
    operator fun invoke (projectId: Int?): Flow<Project?>
    }