package com.example.domain.interactorimpl.milestone

import com.example.domain.interactor.milestone.PutMilestoneToProject
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PutMilestoneToProjectImpl(
    private val milestoneRepository: MilestoneRepository
) : PutMilestoneToProject {

    override suspend operator fun invoke(
        projectId: Int,
        milestone: Milestone
    ): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            milestoneRepository.populateMilestonesByProject(projectId)
        }
        return milestoneRepository.putMilestoneToProject(projectId, milestone)
    }

}