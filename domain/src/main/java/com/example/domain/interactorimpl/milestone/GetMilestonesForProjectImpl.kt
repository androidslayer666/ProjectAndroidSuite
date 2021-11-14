package com.example.domain.interactorimpl.milestone

import com.example.domain.interactor.milestone.GetMilestonesForProject
import com.example.domain.model.Milestone
import com.example.domain.repository.MilestoneRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetMilestonesForProjectImpl(
    private val milestoneRepository: MilestoneRepository
) : GetMilestonesForProject {
    override operator fun invoke(projectId: Int): Flow<List<Milestone>> {
        CoroutineScope(Dispatchers.IO).launch {
            milestoneRepository.populateMilestonesByProject(projectId)
        }
        return milestoneRepository.getMilestonesByProjectFlow(projectId)
    }
}