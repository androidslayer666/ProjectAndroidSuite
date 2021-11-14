package com.example.domain.interactorimpl.milestone

import com.example.domain.interactor.milestone.GetMilestoneById
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMilestoneByIdImpl(
    private val milestoneRepository: MilestoneRepository
) : GetMilestoneById {
    override operator fun invoke(milestoneId : Int?): Flow<Milestone?> {
        return milestoneId?.let {
              milestoneRepository.getMilestoneByIdFlow(milestoneId)
        } ?: flow<Milestone>{}
    }
}