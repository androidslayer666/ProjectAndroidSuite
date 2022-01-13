package com.example.projectandroidsuite.ui.createeditscreens.milestone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.milestone.DeleteMilestone
import com.example.domain.interactor.milestone.GetMilestoneById
import com.example.domain.interactor.milestone.PutMilestoneToProject
import com.example.domain.interactor.milestone.UpdateMilestone
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Milestone
import com.example.domain.model.User
import com.example.domain.utils.Result
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.utils.validation.MilestoneInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


data class MilestoneCreateState(
    val screenMode: ScreenMode = ScreenMode.CREATE,
    val title: String = "",
    val description: String = "",
    val priority: Boolean = false,
    val responsible: User? = null,
    val userSearchQuery: String = "",
    val endDate: Date = Date(),
    val users: List<User> = emptyList(),
    val milestoneInputState: MilestoneInputState = MilestoneInputState(),
    val milestoneDeletionStatus: Result<String, String>? = null
)


@HiltViewModel
class MilestoneCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val getMilestoneById: GetMilestoneById,
    private val putMilestoneToProject: PutMilestoneToProject,
    private val updateMilestone: UpdateMilestone,
    private val deleteMilestone: DeleteMilestone
) : ViewModel() {

    private val _uiState = MutableStateFlow(MilestoneCreateState())
    val uiState: StateFlow<MilestoneCreateState> = _uiState.asStateFlow()

    private var projectId: Int? = null

    private var milestoneId: Int? = null

    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { users ->
                _uiState.update { it.copy(users = users) }
            }
        }
    }

    fun setProjectId(projectId: Int) {
        this.projectId = projectId
    }

    fun setMilestone(milestoneId: Int) {
        if(milestoneId > 0 ) {
            this.milestoneId = milestoneId

            viewModelScope.launch(IO) {
                getMilestoneById(milestoneId).collectLatest { milestone ->
                    _uiState.value = MilestoneCreateState(
                        title = milestone?.title ?: "",
                        priority = milestone?.isKey ?: false,
                        description = milestone?.description ?: "",
                        endDate = milestone?.deadline ?: Date(),
                        responsible = milestone?.responsible,
                        screenMode = ScreenMode.EDIT
                    )
                }
            }
        }
    }


    fun setTitle(text: String) {
        _uiState.update { it.copy(title = text) }
    }


    fun setDescription(text: String) {
        _uiState.update { it.copy(description = text) }
    }

    fun setDate(date: Date) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun setResponsible(user: User) {

        _uiState.update { it.copy(responsible = user) }
    }

    fun setPriority(value: Boolean) {
        _uiState.update { it.copy(priority = value) }
    }

    fun setUserSearch(query: String) {
        _uiState.update { it.copy(userSearchQuery = query) }
        getAllUsers.setFilter(query)
    }

    fun clearInput() {
        _uiState.value = MilestoneCreateState()
        milestoneId = null
        projectId = null
    }

    fun createMilestone() {
        validateInput {
            viewModelScope.launch(IO) {
                val response = putMilestoneToProject(
                    projectId ?: 0,

                    constructMilestone()
                )

                _uiState.update {
                    it.copy(
                        milestoneInputState = it.milestoneInputState.copy(
                            serverResponse = response
                        )
                    )
                }
            }
        }
    }

    fun updateMilestone() {
        validateInput {
            viewModelScope.launch(IO) {
                val response = updateMilestone(
                    projectId ?: 0,
                    constructMilestone()
                )
                _uiState.update {
                    it.copy(
                        milestoneInputState = it.milestoneInputState.copy(
                            serverResponse = response
                        )
                    )
                }
            }
        }
    }


    fun deleteMilestone() {
        CoroutineScope(IO).launch {
            val status = deleteMilestone(milestoneId, projectId)
            _uiState.update { it.copy(milestoneDeletionStatus = status) }
        }
    }


    private fun validateInput(onSuccess: () -> Unit) {
        when {
            _uiState.value.title.isEmpty() -> _uiState.update {
                it.copy(
                    milestoneInputState = it.milestoneInputState.copy(
                        isTitleEmpty = true
                    )
                )
            }
            _uiState.value.responsible == null -> _uiState.update {
                it.copy(
                    milestoneInputState = it.milestoneInputState.copy(
                        isResponsibleEmpty = true
                    )
                )
            }
            else -> onSuccess()
        }
    }

    private fun constructMilestone(): Milestone {
        return Milestone(
            id = milestoneId ?: 0,
            title = uiState.value.title,
            responsible = uiState.value.responsible,
            description = uiState.value.description,
            isKey = uiState.value.priority,
            deadline = uiState.value.endDate,
        )
    }

}
