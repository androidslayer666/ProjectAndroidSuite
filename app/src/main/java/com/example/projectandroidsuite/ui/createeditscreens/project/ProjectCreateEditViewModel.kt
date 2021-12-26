package com.example.projectandroidsuite.ui.createeditscreens.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.interactor.project.CreateProject
import com.example.domain.interactor.project.DeleteProject
import com.example.domain.interactor.project.GetProjectById
import com.example.domain.interactor.project.UpdateProject
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.utils.Result
import com.example.domain.utils.getListUserIdsFromList
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.utils.getUserById
import com.example.projectandroidsuite.ui.utils.validation.ProjectInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectCreateState(
    val screenMode: ScreenMode = ScreenMode.CREATE,
    val title: String = "",
    val description: String = "",
    val responsible: User? = null,
    val chosenUserList: MutableList<User>? = mutableListOf(),
    val userSearchQuery: String = "",
    val users: List<User>? = null,
    val projectStatus: ProjectStatus? = null,
    val projectInputState: ProjectInputState = ProjectInputState(),
    val projectDeletionStatus: Result<String, String>? = null
)


@HiltViewModel
class ProjectCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createProject: CreateProject,
    private val updateProject: UpdateProject,
    private val getProjectById: GetProjectById,
    private val deleteProject: DeleteProject
) : ViewModel() {

    private var projectId: Int? = null

    private var _uiState = MutableStateFlow(ProjectCreateState())
    val uiState: StateFlow<ProjectCreateState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { listUsers ->
                _uiState.update { it.copy(users = listUsers) }
            }
        }
    }

    fun setProject(projectId: Int) {
        this.projectId = projectId
        viewModelScope.launch(IO) {
            getProjectById(projectId).collectLatest { project ->
                if (project != null)
                    updateProjectInUistate(project)
                getAllUsers.setChosenUsersList(uiState.value.chosenUserList)
            }
        }
    }


    fun setTitle(text: String) {
        _uiState.update { it.copy(title = text) }
        if (text.isNotEmpty())
            _uiState.update {
                it.copy(projectInputState = it.projectInputState.copy(isTitleEmpty = false))
            }
    }

    fun setDescription(text: String) {
        _uiState.update { it.copy(description = text) }
    }

    fun setResponsible(user: User) {
        //Log.d("setResponsible", "setResponsible")
        _uiState.update { it.copy(responsible = user) }
        //_responsible.value = user
        _uiState.update {
            it.copy(projectInputState = it.projectInputState.copy(isResponsibleEmpty = false))
        }
    }

    fun setProjectStatus(status: ProjectStatus) {
        _uiState.update { it.copy(projectStatus = status) }
    }

    fun clearInput() {
        projectId = null

        _uiState.value = ProjectCreateState()
    }

    fun addOrRemoveUser(user: User) {
        val listIds = uiState.value.chosenUserList?.getListUserIdsFromList()
        if (listIds?.contains(user.id) == true) {
            uiState.value.chosenUserList?.remove(
                uiState.value.chosenUserList!!.getUserById(
                    user.id
                )
            )
        } else {
            uiState.value.chosenUserList?.add(user)
        }
        getAllUsers.setChosenUsersList(uiState.value.chosenUserList)
        if(uiState.value.chosenUserList?.isEmpty() != true)
            _uiState.update {
                it.copy(projectInputState = it.projectInputState.copy(isTeamEmpty = false))
            }
    }

    fun setUserSearch(query: String) {
        _uiState.update { it.copy(userSearchQuery = query) }
        getAllUsers.setFilter(query)
    }

    fun createProject() {
        validateInput {
            CoroutineScope(IO).launch {
                val response = createProject(
                    project = constructProject()
                )
                _uiState.update {
                    it.copy(
                        projectInputState = ProjectInputState(
                            serverResponse = response
                        )
                    )
                }
            }
        }
    }


    fun updateProject() {
        validateInput {
            CoroutineScope(IO).launch {
                val response = updateProject(
                    projectId = projectId ?: 0,
                    project = constructProject(),
                    projectStatus = uiState.value.projectStatus
                )
                _uiState.update {
                    it.copy(
                        projectInputState = ProjectInputState(
                            serverResponse = response
                        )
                    )
                }
            }
        }
    }

    fun deleteProject() {
        projectId?.let {
            CoroutineScope(IO).launch {
                val response = deleteProject(projectId!!)
                _uiState.update { it.copy(projectDeletionStatus = response) }
            }
        }
    }

    private fun validateInput(onSuccess: () -> Unit) {
        when {
            _uiState.value.title.isEmpty() -> _uiState.update {
                it.copy(
                    projectInputState = it.projectInputState.copy(
                        isTitleEmpty = true
                    )
                )
            }
            _uiState.value.chosenUserList?.isEmpty() == true -> _uiState.update {
                it.copy(
                    projectInputState = it.projectInputState.copy(isTeamEmpty = true)
                )
            }
            _uiState.value.responsible == null -> _uiState.update {
                it.copy(
                    projectInputState = it.projectInputState.copy(
                        isResponsibleEmpty = true
                    )
                )
            }
            else -> onSuccess()
        }
    }

    private fun constructProject()
            : Project {
        return Project(
            id = projectId ?: 0,
            title = uiState.value.title,
            description = uiState.value.description,
            team = uiState.value.chosenUserList,
            responsible = uiState.value.responsible
        )
    }

    private fun updateProjectInUistate(project: Project) {
        _uiState.update {
            it.copy(
                title = project.title,
                description = project.description,
                responsible = project.responsible,
                chosenUserList = project.team,
                projectStatus = project.status,
                screenMode = ScreenMode.EDIT
            )
        }
    }

}
