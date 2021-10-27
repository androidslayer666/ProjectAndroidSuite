package com.example.projectandroidsuite.ui.projectpage

import androidx.lifecycle.*
import com.example.domain.*
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Project
import com.example.domain.model.User
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
    private val getAllUsers: GetAllUsers
) : ViewModel() {

    private var _statusForFilteringProject = MutableLiveData<ProjectStatus?>()
    val stageForFilteringProject: LiveData<ProjectStatus?> = _statusForFilteringProject

    private var _userForFilteringProject = MutableLiveData<User?>()
    val userForFilteringProject: LiveData<User?> = _userForFilteringProject

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchProject: LiveData<String> = _userSearchQuery

    private var _projectSorting = MutableLiveData(ProjectSorting.STAGE_ASC)
    val projectSorting: LiveData<ProjectSorting> = _projectSorting

    private var _projects = MutableStateFlow<List<Project>>(listOf())
    val projects: StateFlow<List<Project>> = _projects

    private var _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users

    init {
        viewModelScope.launch(IO) {
            getAllProjects().collectLatest { _projects.value = it }
        }
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { _users.value = it }
        }
    }

    fun setProjectSorting(sorting: ProjectSorting) {
        _projectSorting.value = sorting
        getAllProjects.setProjectSorting(sorting)
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllUsers.setFilter(query)
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
