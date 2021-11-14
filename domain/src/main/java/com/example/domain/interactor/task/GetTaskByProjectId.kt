package com.example.domain.interactor.task

import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface GetTaskByProjectId {
    operator fun invoke(projectId: Int): Flow<List<Task>>

    }