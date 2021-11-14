package com.example.domain.interactorimpl.milestone


import com.example.domain.filters.milestone.MilestoneFilter
import com.example.domain.filters.milestone.filterMilestoneByFilter
import com.example.domain.interactor.milestone.GetAllMilestones
import com.example.domain.model.Milestone
import com.example.domain.model.Project
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.transform as transform1

class GetAllMilestonesImpl(

    private val projectRepository: ProjectRepository,
    private val milestoneRepository: MilestoneRepository
) : GetAllMilestones{

    private var milestoneFilter = MutableStateFlow<MilestoneFilter?>(null)

    override suspend operator fun invoke(): Flow<List<Milestone>> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getAllStoredProjects().transform1<List<Project>, Unit> { projects ->
                projects.forEach { project ->
                    milestoneRepository.populateMilestonesByProject(project.id)
                }
            }.collectLatest {  }
        }
        return milestoneRepository.getMilestones().combine(milestoneFilter) { milestones, filter ->
            milestones.filterMilestoneByFilter(filter)
        }
    }


    override fun setFilter(searchQuery: String?) {
        milestoneFilter.value = MilestoneFilter(searchQuery)
    }

}