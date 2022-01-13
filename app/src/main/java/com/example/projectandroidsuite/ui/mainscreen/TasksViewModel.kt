package com.example.projectandroidsuite.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.filters.task.TaskFilter
import com.example.domain.filters.task.TaskStatus
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.sorting.TaskSorting
import com.example.domain.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class TaskScreenState(
    val stageForFilteringTask: TaskStatus? = null,
    val userForFilteringTask: User? = null,
    val userSearchQuery: String = "",
    val taskSorting: TaskSorting = TaskSorting.STAGE_ASC,
    val tasks: List<Task> = emptyList(),
    val users: List<User> = emptyList()
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasks: GetAllTasks,
    private val getAllUsers: GetAllUsers,
) : ViewModel(){

    private val _uiState = MutableStateFlow(TaskScreenState())
    val uiState: StateFlow<TaskScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTasks().collectLatest {
                    list -> _uiState.update { it.copy(tasks = list) }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllUsers().collectLatest {
                    list -> _uiState.update { it.copy(users = list) }
            }
        }
    }

    fun setTaskSorting(sorting: TaskSorting) {
        _uiState.update { it.copy(taskSorting = sorting) }
        getAllTasks.setTaskSorting(sorting)
    }

    fun setUserForFilteringTask(user: User?) {
        _uiState.update { it.copy(userForFilteringTask = user) }
        getAllTasks.setFilter(user = user)
    }

    fun setStatusForFilteringTask(status: TaskStatus?) {
        log(status)
        _uiState.update { it.copy(stageForFilteringTask = status) }
        getAllTasks.setFilter(status = status)
    }

    fun setUserSearch(query: String) {
        _uiState.update { it.copy(userSearchQuery = query) }
        getAllTasks.setFilter(query)
    }

    fun setOverdueTaskFilter( ) {
        getAllTasks.setFilter (status = TaskStatus.ACTIVE, interval = Pair(null, Date()) )
    }

    fun clearFiltersTask() {
        setUserForFilteringTask(null)
        setStatusForFilteringTask(null)
    }

}