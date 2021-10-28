package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.lifecycle.*
import com.example.domain.model.Subtask
import com.example.domain.model.User
import com.example.domain.utils.Result
import com.example.domain.interactor.CreateSubtask
import com.example.domain.interactor.user.GetAllUsers
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

    private var _subtaskCreationStatus = MutableStateFlow<Result<String, String>?>(null)
    val subtaskCreationStatus: StateFlow<Result<String, String>?> = _subtaskCreationStatus

    private var _subtaskUpdatingStatus = MutableStateFlow<Result<String, String>?>(null)
    val subtaskUpdatingStatus: StateFlow<Result<String, String>?> = _subtaskUpdatingStatus

    private var _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users

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
        _subtaskCreationStatus.value = null
        _subtaskUpdatingStatus.value = null
    }

    fun createSubtask() {
        viewModelScope.launch(IO) {
            _subtaskCreationStatus.value = createSubtask(
                Subtask(
                    id = 0,
                    title = title.value ?: "",
                    responsible = responsible.value,
                    taskId = taskId ?: 0
                )
            )
        }
    }
}