package com.example.projectandroidsuite.ui.projectpage

import androidx.lifecycle.*
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.utils.TaskSorting
import com.example.domain.utils.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasks: GetAllTasks,
    private val getAllUsers: GetAllUsers
) : ViewModel(){

    private var _stageFroFilteringTask = MutableStateFlow<TaskStatus?>(null)
    val stageForFilteringTask: StateFlow<TaskStatus?> = _stageFroFilteringTask

    private var _userForFilteringTask = MutableStateFlow<User?>(null)
    val userForFilteringTask: StateFlow<User?> = _userForFilteringTask

    private var _taskSorting = MutableStateFlow(TaskSorting.DEADLINE_ASC)
    val taskSorting: StateFlow<TaskSorting> = _taskSorting

    private var _tasks = MutableStateFlow<List<Task>>(listOf())
    val tasks: StateFlow<List<Task>> = _tasks

    private var _userSearchQuery = MutableStateFlow<String>("")
    val userSearchProject: StateFlow<String> = _userSearchQuery

    private var _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTasks().collectLatest {
                _tasks.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllUsers().collectLatest { _users.value = it }
        }
    }

    fun setTaskSorting(sorting: TaskSorting) {
        _taskSorting.value = sorting
            getAllTasks.setTaskSorting(sorting)
    }

    fun setUserForFilteringTask(user: User?) {
        _userForFilteringTask.value = user
        getAllTasks.setFilter(user = user)
    }

    fun setStatusForFilteringTask(status: TaskStatus?) {
        _stageFroFilteringTask.value = status
        getAllTasks.setFilter(status = status)
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllUsers.setFilter(query)
    }

    fun clearFiltersTask() {
        setUserForFilteringTask(null)
        setStatusForFilteringTask(null)
    }

}