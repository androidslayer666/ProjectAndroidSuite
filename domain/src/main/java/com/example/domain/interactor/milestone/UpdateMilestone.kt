package com.example.domain.interactor.milestone

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateMilestone(
    private val milestoneRepository: MilestoneRepository
) {

    suspend operator fun invoke(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            milestoneRepository.populateMilestonesByProject(projectId)
        }
        return milestoneRepository.putMilestoneToProject(projectId, milestone)
    }

}