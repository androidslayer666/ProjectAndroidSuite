package com.example.domain.interactor.milestone

import com.example.domain.model.Milestone
import kotlinx.coroutines.flow.Flow

interface GetAllMilestones {
    suspend operator fun invoke(): Flow<List<Milestone>>
    fun setFilter(searchQuery: String? = null)
    }