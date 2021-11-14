package com.example.domain.interactorimpl.milestone

import com.example.domain.interactor.milestone.DeleteMilestone
import com.example.domain.repository.MilestoneRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteMilestoneImpl(
    private val milestoneRepository: MilestoneRepository,
) : DeleteMilestone {
    override suspend operator fun invoke(milestoneId: Int?, projectId: Int?): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            milestoneRepository.populateMilestonesByProject(projectId?:0)
        }
        return milestoneRepository.deleteMilestone(milestoneId?:0, projectId)
    }
}