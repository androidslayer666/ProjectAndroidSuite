package com.example.domain.interactor.milestone

import com.example.domain.model.Milestone
import com.example.domain.utils.Result

interface PutMilestoneToProject {

    suspend operator fun invoke(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String>
}