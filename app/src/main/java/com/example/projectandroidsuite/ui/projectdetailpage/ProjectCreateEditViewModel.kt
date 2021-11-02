package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.project.CreateProject
import com.example.domain.interactor.project.UpdateProject
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.utils.ProjectStatus
import com.example.domain.utils.getListUserIdsFromList
import com.example.projectandroidsuite.ui.utils.getUserById
import com.example.projectandroidsuite.ui.utils.validation.ProjectInputState
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

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private var _responsible = MutableStateFlow<User?>(null)
    val responsible: StateFlow<User?> = _responsible

    private var _userSearchQuery = MutableStateFlow<String?>(null)
    val userSearchQuery: StateFlow<String?> = _userSearchQuery

    private var _projectStatus = MutableStateFlow<ProjectStatus?>(null)
    val projectStatus: StateFlow<ProjectStatus?> = _projectStatus

    private var _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    private var _chosenUserList = MutableStateFlow<MutableList<User>>(mutableListOf())
    val chosenUserList: StateFlow<MutableList<User>> = _chosenUserList

    private var _projectInputState = MutableStateFlow(ProjectInputState())
    val projectInputState: StateFlow<ProjectInputState> = _projectInputState

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
        project.team?.let {
            _chosenUserList.value = it.toMutableList()
            updateChosenUsers()
        }
        project.responsible?.let { _responsible.value = project.responsible!! }
        project.status?.let { _projectStatus.value = it }
    }

    fun setTitle(string: String) {
        _title.value = string
        if (string.isNotEmpty()) _projectInputState.value = _projectInputState.value.copy(isTitleEmpty = false)
    }

    fun setDescription(string: String) {
        _description.value = string
    }

    fun setResponsible(user: User) {
        //Log.d("setResponsible", "setResponsible")
        _responsible.value = user
        _projectInputState.value = _projectInputState.value.copy(isResponsibleEmpty = false)
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
        _projectInputState.value = ProjectInputState()
        updateChosenUsers()
    }

    fun addOrRemoveUser(user: User) {
        if (_chosenUserList.value.getListUserIdsFromList().contains(user.id)) {
            _chosenUserList.value.remove(_chosenUserList.value.getUserById(user.id))
        } else {
            _chosenUserList.value.add(user)
            _projectInputState.value = _projectInputState.value.copy(isTeamEmpty = false)
        }
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllUsers.setFilter(query)
    }

    fun updateChosenUsers() {
        getAllUsers.setChosenUsersList(_chosenUserList.value)
    }

    fun createProject() {
        validateInput {
            CoroutineScope(IO).launch {
                val response = createProject(
                    Project(
                        id = 0,
                        title = title.value,
                        description = description.value,
                        team = chosenUserList.value,
                        responsible = responsible.value
                    )
                )
                _projectInputState.value = ProjectInputState(serverResponse = response)
            }
        }
    }

    fun updateProject() {
        validateInput{
            CoroutineScope(IO).launch {
                val response = updateProject(
                    projectId ?: 0,
                    Project(
                        id = 0,
                        title = title.value,
                        description = description.value,
                        team = chosenUserList.value,
                        responsible = responsible.value
                    ),
                    projectStatus.value
                )
                _projectInputState.value = ProjectInputState(serverResponse =   response)
            }
        }
    }

    private fun validateInput (onSuccess: ()->Unit) {
        when{
            title.value.isEmpty() -> _projectInputState.value = _projectInputState.value.copy(isTitleEmpty = true)
            chosenUserList.value.size < 1 -> _projectInputState.value = _projectInputState.value.copy(isTeamEmpty = true)
            responsible.value == null -> _projectInputState.value = _projectInputState.value.copy(isResponsibleEmpty = true)
            else ->  onSuccess()
        }
    }
}

