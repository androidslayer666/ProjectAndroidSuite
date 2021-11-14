package com.example.domain.interactorimpl.project

import com.example.domain.filters.project.ProjectFilter
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.filters.project.filterProjectsByFilter
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.repository.ProjectRepository
import com.example.domain.sorting.ProjectSorting
import com.example.domain.sorting.sortProjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetAllProjectsImpl(
    private val projectRepository: ProjectRepository
) : GetAllProjects {

    private var projectFilter = MutableStateFlow<ProjectFilter?>(null)

    private var projectSorting = MutableStateFlow(ProjectSorting.STAGE_ASC)

    override suspend operator fun invoke(): Flow<List<Project>> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getProjects()
        }
        return projectRepository.getAllStoredProjects().combine(projectFilter) { projects, filter ->
            projects.filterProjectsByFilter(filter)
        }.combine(projectSorting) { projects, sorting ->
            projects.sortProjects(sorting)
        }
    }

    override fun setFilter(searchQuery: String?, status: ProjectStatus?, user: User?) {
        searchQuery?.let {
            projectFilter.value = ProjectFilter(
                searchQuery = searchQuery,
                status = projectFilter.value?.status,
                responsible = projectFilter.value?.responsible
            )
        }
        status?.let {
            projectFilter.value = ProjectFilter(
                searchQuery = projectFilter.value?.searchQuery,
                status = status,
                responsible = projectFilter.value?.responsible
            )
        }
        user?.let {
            projectFilter.value = ProjectFilter(
                searchQuery = projectFilter.value?.searchQuery,
                status = projectFilter.value?.status,
                responsible = user
            )
        }
        if (searchQuery == null && status == null && user == null) {
            projectFilter.value = null
        }
    }

    override fun setProjectSorting(sorting: ProjectSorting) {
        projectSorting.value = sorting
    }

}