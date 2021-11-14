package com.example.domain.interactor.milestone

import com.example.domain.utils.Result

interface DeleteMilestone {
    suspend operator fun invoke(milestoneId: Int?, projectId: Int?): Result<String, String>

    }