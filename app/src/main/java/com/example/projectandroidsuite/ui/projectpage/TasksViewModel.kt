package com.example.projectandroidsuite.ui.projectpage

import androidx.lifecycle.*
import com.example.domain.*
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.repository.TaskRepository
import com.example.domain.repository.TeamRepository
import com.example.projectandroidsuite.logic.combineWith
import com.example.projectandroidsuite.logic.forceRefresh
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasks: GetAllTasks,
    private val getAllUsers: GetAllUsers
) : ViewModel(){

    private var _stageFroFilteringTask = MutableLiveData<TaskStatus?>()
    val stageForFilteringTask: LiveData<TaskStatus?> = _stageFroFilteringTask

    private var _userForFilteringTask = MutableLiveData<User?>()
    val userForFilteringTask: LiveData<User?> = _userForFilteringTask

    private var _taskSorting = MutableLiveData(TaskSorting.DEADLINE_ASC)
    val taskSorting: LiveData<TaskSorting> = _taskSorting

    private var _tasks = MutableStateFlow<List<Task>>(listOf())
    val tasks: StateFlow<List<Task>> = _tasks

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchProject: LiveData<String> = _userSearchQuery

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