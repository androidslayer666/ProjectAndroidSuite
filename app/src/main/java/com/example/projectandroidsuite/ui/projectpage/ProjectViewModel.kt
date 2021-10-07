package com.example.projectandroidsuite.ui.projectpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.UserEntity
import com.example.domain.repository.Failure
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.repository.TeamRepository
import com.example.projectandroidsuite.logic.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    repository: ProjectRepository,
    taskRepository: TaskRepository,
    teamRepository: TeamRepository
) : ViewModel() {

    private var _stageFroFilteringProject = MutableLiveData<ProjectStatus?>()
    val stageForFilteringProject: LiveData<ProjectStatus?> = _stageFroFilteringProject

    private var _stageFroFilteringTask = MutableLiveData<TaskStatus?>()
    val stageForFilteringTask: LiveData<TaskStatus?> = _stageFroFilteringTask

    private var _userForFilteringProject = MutableLiveData<UserEntity?>()
    val userForFilteringProject: LiveData<UserEntity?> = _userForFilteringProject

    private var _userForFilteringTask = MutableLiveData<UserEntity?>()
    val userForFilteringTask: LiveData<UserEntity?> = _userForFilteringTask

    private var userSearch = MutableLiveData<UserFilter>()

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchProject: LiveData<String> = _userSearchQuery

    private var projectFilter = MutableLiveData<ProjectFilter?>()

    private var taskFilter = MutableLiveData(TaskFilter(status = TaskStatus.ACTIVE))

    private var _problemWithFetchingProjects = MutableLiveData<String>()
    val problemWithFetchingProjects: LiveData<String> = _problemWithFetchingProjects

    private var _projectSorting = MutableLiveData(ProjectSorting.STAGE_ASC)
    val projectSorting: LiveData<ProjectSorting> = _projectSorting

    private var _taskSorting = MutableLiveData(TaskSorting.DEADLINE_ASC)
    val taskSorting: LiveData<TaskSorting> = _taskSorting


    val projects =
        repository.projectsFromDb.asLiveData().combineWith(projectFilter) { listProjects, filter ->
                if (filter != null) {
                    listProjects?.filterProjectsByFilter(filter)
                } else {
                    listProjects
                }
        }.combineWith(projectSorting) { listProjects, sorting ->
            listProjects?.sortProjects(sorting)
        }

    val tasks = taskRepository.getAllUserTasks().asLiveData().combineWith(taskFilter){
        listTasks, filter ->
        if(filter != null) {
            listTasks?.filterTaskByFilter(filter)
        }else {
            listTasks
        }
    }.combineWith(taskSorting) { listTasks, sorting ->
        listTasks?.sortTasks(sorting)
    }

    val userListFlow = teamRepository.getAllPortalUsers().asLiveData()
        .combineWith(userSearch) { listUsers, filter ->
            if (filter != null) listUsers?.filterUsersByFilter(filter)
            else listUsers
        }

    init {
        Log.d("ProjectViewModel", this.toString())
        viewModelScope.launch(IO) {
            val projectResponse = repository.getProjects()
            if(projectResponse is Failure<String>) _problemWithFetchingProjects.value = projectResponse.reason!!
            taskRepository.populateTasks()
            teamRepository.populateAllPortalUsers()
        }
    }

    fun setProjectSorting(sorting: ProjectSorting) {
        _projectSorting.value = sorting
    }

    fun setTaskSorting(sorting: TaskSorting) {
        _taskSorting.value = sorting
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value = UserFilter(query)
    }

    fun setUserForFilteringProject(user: UserEntity) {
        _userForFilteringProject.value = user
        if(projectFilter.value != null){
            projectFilter.value!!.responsible = user.id
            projectFilter.forceRefresh()
        } else {
            projectFilter.value = ProjectFilter(responsible = user.id)
        }
    }

    fun setUserForFilteringTask(user: UserEntity) {
        _userForFilteringTask.value = user
        if(taskFilter.value != null){
            taskFilter.value!!.responsible = user.id
            taskFilter.forceRefresh()
        } else {
            taskFilter.value = TaskFilter(responsible = user.id)
        }
    }

    fun setStageForFiltering(status: ProjectStatus) {
        _stageFroFilteringProject.value = status
        if(projectFilter.value != null){
            projectFilter.value!!.status = status
            projectFilter.forceRefresh()
        } else {
            projectFilter.value = ProjectFilter(status = status)
        }
    }

    fun setStageForFilteringTask(status: TaskStatus) {
        _stageFroFilteringTask.value = status
        if(taskFilter.value != null){
            taskFilter.value!!.status = status
            taskFilter.forceRefresh()
        } else {
            taskFilter.value = TaskFilter(status = status)
        }
    }

    fun clearFiltersProject() {
        projectFilter.value = null
        _userForFilteringProject.value = null
        _stageFroFilteringProject.value = null
    }

    fun clearFiltersTask() {
        taskFilter.value = null
        _userForFilteringTask.value = null
        _stageFroFilteringTask.value = null
    }
}
