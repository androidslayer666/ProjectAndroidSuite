package com.example.domain.interactor.milestone

import com.example.domain.model.Milestone
import kotlinx.coroutines.flow.Flow

interface GetMilestonesForProject {
    operator fun invoke(projectId: Int): Flow<List<Milestone>>
    }