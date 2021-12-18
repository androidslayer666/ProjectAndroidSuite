package com.example.projectandroidsuite.ui.mainscreen

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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectScreenState(
    val stageForFilteringProject: ProjectStatus? = null,
    val userForFilteringProject: User? = null,
    val userSearchProject: String = "",
    val projectSorting: ProjectSorting = ProjectSorting.STAGE_ASC,
    val projects: List<Project> = emptyList(),
    val users: List<User> = emptyList()
)

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getAllProjects: GetAllProjects,
    private val getAllUsers: GetAllUsers,

) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectScreenState())
    val uiState: StateFlow<ProjectScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            getAllProjects().collectLatest { list -> _uiState.update { it.copy(projects = list) } }
        }
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest {
                list -> _uiState.update { it.copy(users = list) }
            }
        }
    }

    fun setProjectSorting(sorting: ProjectSorting) {
        _uiState.update { it.copy(projectSorting = sorting) }
        getAllProjects.setProjectSorting(sorting)
    }

    fun setUserSearch(query: String) {
        _uiState.update { it.copy(userSearchProject = query) }
        getAllProjects.setFilter(query)
    }

    fun setUserForFilteringProjects(user: User?) {
        _uiState.update { it.copy(userForFilteringProject = user) }
        getAllProjects.setFilter(user = user)
    }

    fun setStatusForFilteringProjects(status: ProjectStatus?) {
        _uiState.update { it.copy(stageForFilteringProject = status) }
        getAllProjects.setFilter(status = status)
    }

    fun clearFiltersProject() {
        setUserForFilteringProjects(null)
        setStatusForFilteringProjects(null)
    }
}
