package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.project.CreateProject
import com.example.domain.interactor.project.UpdateProject
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.utils.ProjectStatus
import com.example.domain.utils.Result
import com.example.domain.utils.getListUserIdsFromList
import com.example.projectandroidsuite.ui.utils.getUserById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createProject: CreateProject,
    private val updateProject: UpdateProject,
) : ViewModel() {

    private var projectId: Int? = null

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private var _responsible = MutableLiveData<User?>()
    val responsible: LiveData<User?> = _responsible

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchQuery: LiveData<String> = _userSearchQuery

    private var _projectStatus = MutableLiveData<ProjectStatus>()
    val projectStatus: LiveData<ProjectStatus> = _projectStatus

    private var _projectCreationStatus = MutableStateFlow<Result<String, String>?>(null)
    val projectCreationStatus: StateFlow<Result<String, String>?> = _projectCreationStatus

    private var _projectUpdatingStatus = MutableStateFlow<Result<String, String>?>(null)
    val projectUpdatingStatus: StateFlow<Result<String, String>?> = _projectUpdatingStatus

    private var _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    private var _chosenUserList = MutableStateFlow<MutableList<User>>(mutableListOf())
    val chosenUserList: StateFlow<MutableList<User>> = _chosenUserList


    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { listUsers ->
                _users.value = listUsers
            }
        }
    }

    fun setProject(project: Project) {
        projectId = project.id
        _title.value = project.title
        _description.value = project.description
        project.team?.let { _chosenUserList.value = project.team!! }
        project.responsible?.let { _responsible.value = project.responsible!! }
        project.status?.let { _projectStatus.value = it}
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
        if (_chosenUserList.value.getListUserIdsFromList().contains(user.id)) {
            _chosenUserList.value.remove(_chosenUserList.value.getUserById(user.id))
        } else {
            _chosenUserList.value.add(user)
        }
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllUsers.setFilter(query)
    }

    fun updateChosenUsers(){
        getAllUsers.setChosenUsersList(_chosenUserList.value)
    }

    fun createProject() {
        CoroutineScope(IO).launch {
            val response = createProject(
                Project(
                    id = 0,
                    title = title.value ?: "",
                    description = description.value ?: "",
                    team = chosenUserList.value,
                    responsible = responsible.value
                )
            )
            _projectCreationStatus.value = response
        }
    }

    fun updateProject() {
        CoroutineScope(IO).launch {
            val response = updateProject(
                projectId ?: 0,
                Project(
                    id = 0,
                    title = title.value ?: "",
                    description = description.value ?: "",
                    team = chosenUserList.value,
                    responsible = responsible.value
                ),
                projectStatus.value
            )
            _projectUpdatingStatus.value = response
        }
    }
}

