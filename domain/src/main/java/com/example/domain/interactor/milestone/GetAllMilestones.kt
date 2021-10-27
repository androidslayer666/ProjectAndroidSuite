package com.example.domain.interactor.milestone

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.MilestoneFilter
import com.example.domain.TaskFilter
import com.example.domain.entities.MilestoneEntity
import com.example.domain.filterMilestoneByFilter
import com.example.domain.interactor.project.GetAllProjects
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

class GetAllMilestones(

    private val projectRepository: ProjectRepository,
    private val milestoneRepository: MilestoneRepository
) {

    private var milestoneFilter = MutableStateFlow<MilestoneFilter?>(null)

    suspend operator fun invoke(): Flow<List<Milestone>> {
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


    fun setFilter(searchQuery: String? = null) {
        milestoneFilter.value = MilestoneFilter(searchQuery)
    }

}