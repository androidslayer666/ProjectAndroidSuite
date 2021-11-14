package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.task.CreateSubtask
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Subtask
import com.example.domain.model.User
import com.example.projectandroidsuite.ui.utils.validation.SubtaskInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtaskCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createSubtask: CreateSubtask
) : ViewModel() {

    private var taskId: Int? = null

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _responsible = MutableStateFlow<User?>(null)
    val responsible: StateFlow<User?> = _responsible

    private var _userSearchQuery = MutableStateFlow("")
    val userSearchQuery: StateFlow<String> = _userSearchQuery


    private var _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users

    private var _subtaskInputState = MutableStateFlow(SubtaskInputState())
    val subtaskInputState: StateFlow<SubtaskInputState> = _subtaskInputState

    init{
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { _users.value = it }
        }
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllUsers.setFilter(query)
    }

    fun setSubtask(subtask: Subtask) {
        //Log.d("setTask", task.toString())
        taskId = subtask.id
        _title.value = subtask.title?:""
        subtask.responsible?.let { _responsible.value = it }

    }

    fun setTitle(string: String) {
        _title.value = string
    }

    fun setTaskId(taskId: Int) {
        this.taskId = taskId
    }


    fun setResponsible(user: User) {
        _responsible.value = user
    }

    fun clearInput() {
        _title.value = ""
        _responsible.value = null
        _subtaskInputState.value = SubtaskInputState()
    }

    fun createSubtask() {
        validateInput {
            viewModelScope.launch(IO) {
                val response = createSubtask(
                    Subtask(
                        id = 0,
                        title = title.value ?: "",
                        responsible = responsible.value,
                        taskId = taskId ?: 0
                    )
                )
                _subtaskInputState.value = SubtaskInputState(serverResponse = response)
            }
        }
    }

    private fun validateInput (onSuccess: ()->Unit) {
        when{
            title.value.isEmpty() -> _subtaskInputState.value = _subtaskInputState.value.copy(isTitleEmpty = true)
            _responsible.value == null -> _subtaskInputState.value = _subtaskInputState.value.copy(isResponsibleEmpty=  true)
            else ->  onSuccess()
        }
    }
}