package com.example.domain.interactor.milestone

import com.example.domain.Result
import com.example.domain.repository.MilestoneRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteMilestone(
    private val milestoneRepository: MilestoneRepository,
) {
    suspend operator fun invoke(milestoneId: Int, projectId: Int?): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            milestoneRepository.populateMilestonesByProject(projectId?:0)
        }
        return milestoneRepository.deleteMilestone(milestoneId, projectId)
    }
}