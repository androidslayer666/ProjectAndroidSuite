package com.example.domain.interactorimpl.milestone

import com.example.domain.interactor.milestone.GetMilestoneById
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow

class GetMilestoneByIdImpl(
    private val milestoneRepository: MilestoneRepository
) : GetMilestoneById {
    override operator fun invoke(milestoneId : Int?): Flow<Milestone?> {
        return milestoneRepository.getMilestoneByIdFlow(milestoneId?:0)
    }
}