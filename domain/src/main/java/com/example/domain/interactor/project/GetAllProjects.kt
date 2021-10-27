package com.example.domain.interactor.project

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.domain.*
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GetAllProjects(
    private val projectRepository: ProjectRepository
) {

    private var projectFilter = MutableStateFlow<ProjectFilter?>(null)

    private var projectSorting = MutableStateFlow(ProjectSorting.STAGE_ASC)

    suspend operator fun invoke(): Flow<List<Project>> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getProjects()
        }
        return projectRepository.getAllStoredProjects().combine(projectFilter) { projects, filter ->
            projects.filterProjectsByFilter(filter)
        }.combine(projectSorting) { projects, sorting ->
            projects.sortProjects(sorting)
        }
    }

    fun setFilter(searchQuery: String? = null, status: ProjectStatus? = null, user: User? = null) {
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

    fun setProjectSorting(sorting: ProjectSorting) {
        projectSorting.value = sorting
    }

}