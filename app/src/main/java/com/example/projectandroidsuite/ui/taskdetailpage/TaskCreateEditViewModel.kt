package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.milestone.GetMilestoneById
import com.example.domain.interactor.milestone.GetMilestonesForProject
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.project.GetProjectById
import com.example.domain.interactor.task.CreateTask
import com.example.domain.interactor.task.UpdateTask
import com.example.domain.model.Milestone
import com.example.domain.model.Project
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.utils.Result
import com.example.domain.utils.TaskStatus
import com.example.domain.utils.UserFilter
import com.example.domain.utils.filterUsersByFilter
import com.example.projectandroidsuite.ui.utils.getListIds
import com.example.projectandroidsuite.ui.utils.getUserById
import com.example.projectandroidsuite.ui.utils.validation.TaskInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskCreateEditViewModel @Inject constructor(
    private val getAllProjects: GetAllProjects,
    private val createTask: CreateTask,
    private val updateTask: UpdateTask,
    private val getProjectById: GetProjectById,
    private val getMilestoneById: GetMilestoneById,
    private val getMilestonesForProject: GetMilestonesForProject
) : ViewModel() {

    private var taskId: Int? = null

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private var _priority = MutableStateFlow(0)
    val priority: StateFlow<Int> = _priority

    private var _chosenUserList = MutableStateFlow<MutableList<User>>(mutableListOf())
    val chosenUserList: StateFlow<MutableList<User>> = _chosenUserList

    private var _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project

    private var _milestone = MutableStateFlow<Milestone?>(null)
    val milestone: StateFlow<Milestone?> = _milestone

    private var userSearch = MutableStateFlow<UserFilter?>(null)

    private var _userSearchQuery = MutableStateFlow("")
    val userSearchQuery: StateFlow<String> = _userSearchQuery

    private var _taskStatus = MutableStateFlow<TaskStatus?>(null)
    val taskStatus: StateFlow<TaskStatus?> = _taskStatus

    private var _projectSearchQuery = MutableStateFlow("")
    val projectSearchQuery: StateFlow<String> = _projectSearchQuery

    private var _taskCreationStatus = MutableStateFlow<Result<String, String>?>(null)
    val taskCreationStatus: StateFlow<Result<String, String>?> = _taskCreationStatus

    private var _taskUpdatingStatus = MutableStateFlow<Result<String, String>?>(null)
    val taskUpdatingStatus: StateFlow<Result<String, String>?> = _taskUpdatingStatus

    private var _endDate = MutableStateFlow(Date())
    val endDate: StateFlow<Date> = _endDate

    private var _projectList = MutableStateFlow<List<Project>>(listOf())
    val projectList: StateFlow<List<Project>> = _projectList

    private var _milestonesList = MutableStateFlow<List<Milestone>?>(null)
    val milestonesList: StateFlow<List<Milestone>?> = _milestonesList

    private var _taskInputState = MutableStateFlow(TaskInputState())
    val taskInputState: StateFlow<TaskInputState> = _taskInputState

    val userList = project.transform { project ->
        emit(project?.team)
    }
        .combine(chosenUserList) { users, chosenUsers ->
            users?.forEach { user ->
                user.chosen = chosenUsers.getListIds().contains(user.id) == true
            }
            users
        }
        .combine(userSearch) { users, filter ->
            users?.filterUsersByFilter(filter)
        }

    init {
        viewModelScope.launch(IO) {
            viewModelScope.launch(IO) {
                getAllProjects().collectLatest { _projectList.value = it }
            }
        }
    }

    fun setTask(task: Task) {
        taskId = task.id
        _title.value = task.title
        _description.value = task.description
        _chosenUserList.value = task.responsibles.toMutableList()
        _endDate.value = task.deadline
        _priority.value = task.priority ?: 0
        task.projectOwner?.let { projectOwner ->
            setProject(projectOwner)
        }
        task.milestoneId?.let {
            viewModelScope.launch {
                getMilestoneById(task.milestoneId!!).collectLatest {
                    withContext(Main) {
                        _milestone.value = it
                    }
                }
            }
        }
    }

    fun setTitle(string: String) {
        _title.value = string
        _taskInputState.value = _taskInputState.value.copy(isTitleEmpty = false)
    }

    fun setDescription(string: String) {
        _description.value = string
    }

    fun setPriority(value: Int) {
        Log.d("setPriority", value.toString())
        _priority.value = value
    }

    fun setStringForFilteringProjects(value: String) {
        _projectSearchQuery.value = value
        getAllProjects.setFilter(searchQuery = value)
    }

    fun setProject(project: Project) {
        _project.value = project

        viewModelScope.launch {
            getProjectById(project.id).collectLatest { project ->
                _project.value = project
            }
        }
        viewModelScope.launch {
            getMilestonesForProject(project.id).collectLatest {
                _milestonesList.value = it
            }
        }
        Log.d("setProject", _project.value.toString())
        _taskInputState.value = _taskInputState.value.copy(isProjectEmpty = false)
    }

    fun setMilestone(milestone: Milestone) {
        _milestone.value = milestone
        Log.d("setProject", _milestone.value.toString())
    }

    fun setTaskStatus(status: TaskStatus) {
        _taskStatus.value = status
    }

    fun setDate(date: Date) {
        Log.d("setDate", date.toString())
        _endDate.value = date
    }

    fun clearInput() {
        _title.value = ""
        _description.value = ""
        _priority.value = 0
        _chosenUserList.value = mutableListOf()
        _project.value = null
        _milestone.value = null
        _taskCreationStatus.value = null
        _taskUpdatingStatus.value = null
    }

    fun addOrRemoveUser(user: User) {
        if (_chosenUserList.value.getListIds().contains(user.id)) {
            _chosenUserList.value.remove(_chosenUserList.value.getUserById(user.id))
        } else {
            _chosenUserList.value.add(user)
            _taskInputState.value = _taskInputState.value.copy(isTeamEmpty = false)
        }
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }


    fun createTask() {
        validateInput {
            viewModelScope.launch(IO) {
                Log.d("createTask", "start creating a task")
                val response = createTask(
                    milestone.value?.id,
                    Task(
                        id = 0,
                        description = description.value,
                        deadline = endDate.value,
                        title = title.value,
                        projectOwner = project.value,
                        responsibles = chosenUserList.value,
                        priority = priority.value,
                        projectId = project.value?.id
                    )
                )
                _taskInputState.value = TaskInputState(serverResponse = response)
            }
        }
    }

    fun updateTask() {
        validateInput {
            viewModelScope.launch(IO) {
                val response = updateTask(
                    taskId,
                    Task(
                        id = 0,
                        description = description.value,
                        deadline = endDate.value,
                        title = title.value,
                        projectOwner = project.value,
                        responsibles = chosenUserList.value,
                        priority = priority.value
                    ),
                    taskStatus.value
                )
                _taskInputState.value = TaskInputState(serverResponse = response)
            }
        }
    }

    private fun validateInput (onSuccess: ()->Unit) {
        when{
            title.value.isEmpty() -> _taskInputState.value = _taskInputState.value.copy(isTitleEmpty = true)
            project.value == null -> _taskInputState.value = _taskInputState.value.copy(isProjectEmpty = true)
            chosenUserList.value.size < 1 -> _taskInputState.value = _taskInputState.value.copy(isTeamEmpty = true)
            else ->  onSuccess()
        }
    }
}

