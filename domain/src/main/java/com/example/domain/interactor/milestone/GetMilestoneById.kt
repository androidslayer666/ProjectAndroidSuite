package com.example.domain.interactor.milestone

import com.example.domain.model.Milestone
import kotlinx.coroutines.flow.Flow

interface GetMilestoneById {
    operator fun invoke(milestoneId : Int?): Flow<Milestone?>
    }