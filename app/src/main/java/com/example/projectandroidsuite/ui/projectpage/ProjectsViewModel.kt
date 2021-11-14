package com.example.projectandroidsuite.ui.projectpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.sorting.ProjectSorting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getAllProjects: GetAllProjects,
    private val getAllUsers: GetAllUsers,
) : ViewModel() {

    private var _statusForFilteringProject = MutableStateFlow<ProjectStatus?>(null)
    val stageForFilteringProject: StateFlow<ProjectStatus?> = _statusForFilteringProject

    private var _userForFilteringProject = MutableStateFlow<User?>(null)
    val userForFilteringProject: StateFlow<User?> = _userForFilteringProject

    private var _userSearchQuery = MutableStateFlow("")
    val userSearchProject: StateFlow<String> = _userSearchQuery

    private var _projectSorting = MutableStateFlow(ProjectSorting.STAGE_ASC)
    val projectSorting: StateFlow<ProjectSorting> = _projectSorting

    private var _projects = MutableStateFlow<List<Project>>(listOf())
    val projects: StateFlow<List<Project>> = _projects

    private var _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users

    init {
        viewModelScope.launch(IO) {
            getAllProjects().collectLatest { _projects.value = it }
        }
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest {
                _users.value = it
            }
        }
    }

    fun setProjectSorting(sorting: ProjectSorting) {
        _projectSorting.value = sorting
        getAllProjects.setProjectSorting(sorting)
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllProjects.setFilter(query)
    }

    fun setUserForFilteringProjects(user: User?) {
        _userForFilteringProject.value = user
        getAllProjects.setFilter(user = userForFilteringProject.value)
    }

    fun setStatusForFilteringProjects(status: ProjectStatus?) {
        _statusForFilteringProject.value = status
        getAllProjects.setFilter(status = stageForFilteringProject.value)
    }

    fun clearFiltersProject() {
        setUserForFilteringProjects(null)
        setStatusForFilteringProjects(null)
    }
}
