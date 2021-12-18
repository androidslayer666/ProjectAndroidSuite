package com.example.projectandroidsuite.ui.createeditscreens.subtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.task.CreateSubtask
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Subtask
import com.example.domain.model.User
import com.example.projectandroidsuite.ui.utils.validation.SubtaskInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubtaskCreateState(
    val title: String = "",
    val responsible: User? = null,
    val userSearchQuery: String = "",
    val users: List<User>? = null,
    val subtaskInputState: SubtaskInputState = SubtaskInputState()
)

@HiltViewModel
class SubtaskCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createSubtask: CreateSubtask
) : ViewModel() {


    private var taskId: Int? = null

    private var _uiState = MutableStateFlow(SubtaskCreateState())
    val uiState: StateFlow<SubtaskCreateState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { users ->
                _uiState.update { it.copy(users = users) }
            }
        }
    }

    fun setUserSearch(query: String) {
        _uiState.update { it.copy(userSearchQuery = query) }
        getAllUsers.setFilter(query)
    }

    fun setSubtask(subtask: Subtask) {
        //Log.d("setTask", task.toString())
        taskId = subtask.id

    }

    fun setTitle(string: String) {
        fun setTitle(text: String) {
            _uiState.update { it.copy(title = text) }
            if (text.isNotEmpty())
                _uiState.update {
                    it.copy(subtaskInputState = it.subtaskInputState.copy(isTitleEmpty = false))
                }
        }
    }

    fun setTaskId(taskId: Int) {

    }

    fun setResponsible(user: User) {
        _uiState.update { it.copy(responsible = user) }
        //_responsible.value = user
        _uiState.update {
            it.copy(subtaskInputState = it.subtaskInputState.copy(isResponsibleEmpty = false))
        }
    }

    fun clearInput() {
        _uiState.value = SubtaskCreateState()
        taskId = null
    }

    fun createSubtask() {
        viewModelScope.launch(IO) {
            val response = createSubtask(
                Subtask(
                    id = 0,
                    title = uiState.value.title ?: "",
                    responsible = uiState.value.responsible,
                    taskId = taskId ?: 0
                )
            )
            _uiState.update { it.copy(subtaskInputState = SubtaskInputState(serverResponse = response)) }
        }
    }


    private fun validateInput(onSuccess: () -> Unit) {


        when {
            _uiState.value.title.isEmpty() -> _uiState.update {
                it.copy(
                    subtaskInputState = it.subtaskInputState.copy(
                        isTitleEmpty = true
                    )
                )
            }

            _uiState.value.responsible == null -> _uiState.update {
                it.copy(
                    subtaskInputState = it.subtaskInputState.copy(
                        isResponsibleEmpty = true
                    )
                )
            }
            else -> onSuccess()
        }
    }
}
