package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.Result
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.repository.*

import com.example.projectandroidsuite.logic.*

import com.example.projectandroidsuite.ui.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProjectCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private var projectId: Int? = null

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private var _chosenUserList = MutableLiveData<MutableList<User>>(mutableListOf())
    val chosenUserList: LiveData<MutableList<User>> = _chosenUserList

    private var _responsible = MutableLiveData<User?>()
    val responsible: LiveData<User?> = _responsible

    private var userSearch = MutableLiveData<UserFilter>()

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchQuery: LiveData<String> = _userSearchQuery

    private var _projectStatus = MutableLiveData<ProjectStatus>()
    val projectStatus: LiveData<ProjectStatus> = _projectStatus

    private var _projectCreationStatus = MutableLiveData<Result<String, String>?>()
    val projectCreationStatus: LiveData<Result<String, String>?> = _projectCreationStatus

    private var _projectUpdatingStatus = MutableLiveData<Result<String, String>?>()
    val projectUpdatingStatus: LiveData<Result<String, String>?> = _projectUpdatingStatus

    val userListFlow = teamRepository.getAllPortalUsers().asLiveData()
        .combineWith(chosenUserList) { users, chosenUsers ->
            Log.d("ProjectCreateEditModel", chosenUsers?.map{it.displayName}.toString())
            users?.forEach { user ->
                user.chosen = chosenUsers?.getListIds()?.contains(user.id) == true
            }
            return@combineWith users
        }
        .combineWith(userSearch) { listProject, filter ->
            if (filter != null) listProject?.filterUsersByFilter(filter)
            else listProject
        }

    init {
        viewModelScope.launch(IO) {
            teamRepository.populateAllPortalUsers()
        }
    }

    fun setProject(project: Project) {
        //Log.d("setTask", project.toString())
        projectId = project.id
        _title.value = project.title
        _description.value = project.description
        project.team?.let { _chosenUserList.value = project.team!! }
        project.responsible?.let { _responsible.value = project.responsible!! }
        project.status?.let {
            when (project.status) {
                0 -> _projectStatus.value = ProjectStatus.ACTIVE
                1 -> _projectStatus.value = ProjectStatus.STOPPED
                2 -> _projectStatus.value = ProjectStatus.PAUSED
            }
        }
    }

    fun setTitle(string: String) {
        _title.value = string
    }

    fun setDescription(string: String) {
        _description.value = string
    }

    fun setResponsible(user: User) {
        _responsible.value = user
    }

    fun setProjectStatus(status: ProjectStatus) {
        _projectStatus.value = status
    }

    fun clearInput() {
        projectId = null
        _title.value = ""
        _description.value = ""
        _chosenUserList.value = mutableListOf()
        _responsible.value = null
        _projectCreationStatus.value = null
        _projectUpdatingStatus.value = null
    }

    fun addOrRemoveUser(user: User) {
        //Log.d("addOrRemoveUser", user.toString())
        val listIds = _chosenUserList.value!!.getListIds()
        if (listIds.contains(user.id)) {
            _chosenUserList.value!!.remove(_chosenUserList.value!!.getUserById(user.id))
            Log.d("addOrRemoveUser", "remove user $user")
            Log.d("addOrRemoveUser", "list users  " + _chosenUserList.value!!.map{it.displayName}.toString())
            _chosenUserList.forceRefresh()
        } else {
            _chosenUserList.value!!.add(user)
            Log.d("addOrRemoveUser", "add user $user")
            _chosenUserList.forceRefresh()
        }
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value = UserFilter(query)
    }

    fun createProject() {
        CoroutineScope(IO).launch {
            val response = projectRepository.createProject(
                Project(
                    id =0,
                    title = title.value?:"",
                    description = description.value?:"",
                    team = chosenUserList.value,
                    responsible = responsible.value
                )
            )
            withContext(Main) {
                Log.d("createProject", response.toString())
                _projectCreationStatus.value = response
            }
        }
    }

    fun updateProject() {
        Log.d("addOrRemoveUser", "list users  " + _chosenUserList.value!!.map{it.displayName}.toString())
        CoroutineScope(IO).launch {
            val response = projectRepository.updateProject(
                projectId ?: 0,
                Project(
                    id =0,
                    title = title.value?:"",
                    description = description.value?:"",
                    team = chosenUserList.value,
                    responsible = responsible.value
                ),
                when (projectStatus.value) {
                    ProjectStatus.ACTIVE -> "open"
                    ProjectStatus.PAUSED -> "Paused"
                    ProjectStatus.STOPPED-> "Closed"
                    else -> "open"
                }
            )

            withContext(Main) {
                Log.d("updateProject", response.toString())
                _projectUpdatingStatus.value = response
            }
        }
    }
}

