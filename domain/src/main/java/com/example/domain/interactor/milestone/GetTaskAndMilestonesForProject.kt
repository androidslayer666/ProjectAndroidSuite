package com.example.domain.interactor.milestone

import com.example.domain.model.Milestone
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface GetTaskAndMilestonesForProject {
    operator fun invoke(projectId: Int): Flow<Map<Milestone?, List<Task>>>
    }