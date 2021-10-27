package com.example.domain.interactor.milestone

import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GetMilestoneById(
    private val milestoneRepository: MilestoneRepository
) {
    operator fun invoke(milestoneId : Int?): Flow<Milestone?> {
        return milestoneId?.let {
              milestoneRepository.getMilestoneByIdFlow(milestoneId)
        } ?: flow<Milestone>{}
    }
}