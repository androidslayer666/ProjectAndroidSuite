package com.example.projectandroidsuite.ui.createeditscreens.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.filters.task.TaskStatus
import com.example.domain.filters.user.UserFilter
import com.example.domain.filters.user.filterUsersByFilter
import com.example.domain.interactor.milestone.GetMilestoneById
import com.example.domain.interactor.milestone.GetMilestonesForProject
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.project.GetProjectById
import com.example.domain.interactor.task.CreateTask
import com.example.domain.interactor.task.DeleteTask
import com.example.domain.interactor.task.GetTaskById
import com.example.domain.interactor.task.UpdateTask
import com.example.domain.model.*
import com.example.domain.utils.Result
import com.example.domain.utils.getListUserIdsFromList
import com.example.projectandroidsuite.ui.createeditscreens.ScreenMode
import com.example.projectandroidsuite.ui.utils.getListIds
import com.example.projectandroidsuite.ui.utils.getUserById
import com.example.projectandroidsuite.ui.utils.validation.ProjectInputState
import com.example.projectandroidsuite.ui.utils.validation.TaskInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

data class TaskCreateState(
    val title: String = "",
    val description: String = "",
    val chosenUserList: MutableList<User>? = mutableListOf(),
    val userSearchQuery: String = "",
    val project: Project? = null,
    val milestone: Milestone? = null,
    val taskStatus: TaskStatus? = null,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val projectSearchQuery: String = "",
    val endDate: Date = Date(),
    val users: List<User>? = null,
    val projectStatus: ProjectStatus? = null,
    val projectInputState: ProjectInputState = ProjectInputState(),
    val projectDeletionStatus: Result<String, String>? = null,
    val projectList: List<Project>? = null,
    val milestonesList: List<Milestone>? = null,
    val taskInputState: TaskInputState = TaskInputState(),
    val taskDeletionStatus: Result<String, String>? = null,
    val screenMode: ScreenMode = ScreenMode.CREATE
)


@HiltViewModel
class TaskCreateEditViewModelNew @Inject constructor(
    private val getAllProjects: GetAllProjects,
    private val getTaskById: GetTaskById,
    private val getMilestoneById: GetMilestoneById,
    private val getProjectById: GetProjectById,
    private val getMilestonesForProject: GetMilestonesForProject,
    private val createTask: CreateTask,
    private val updateTask: UpdateTask,
    private val deleteTask: DeleteTask

) : ViewModel() {


    private var taskId: Int? = null

    private var userSearch = MutableStateFlow<UserFilter?>(null)

    private var _uiState = MutableStateFlow(TaskCreateState())
    val uiState: StateFlow<TaskCreateState> = _uiState.asStateFlow()

    private var _projectList = MutableStateFlow<List<Project>>(listOf())
    val projectList: StateFlow<List<Project>> = _projectList
    val userListState = uiState.transform { state ->
        val projectTeam = state.project?.team
        val chosenUsers = state.chosenUserList

        projectTeam?.forEach { user ->
            user.chosen = chosenUsers?.getListIds()?.contains(user.id) == true
        }

        emit(projectTeam?.filterUsersByFilter(userSearch.value))
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                getAllProjects().collectLatest { _projectList.value = it }
                getAllProjects().collectLatest { listProjects ->
                    _uiState.update { it.copy(projectList = listProjects) }

                }
            }
        }
    }

    fun setTask(taskId: Int) {
        viewModelScope.launch {
            this@TaskCreateEditViewModelNew.taskId = taskId
            getTaskById(taskId).collectLatest { task ->
                if (task != null) {
                    _uiState.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            chosenUserList = task.responsibles.toMutableList(),
                            endDate = task.deadline,
                            project = task.projectOwner,
                            screenMode = ScreenMode.EDIT
                        )
                    }
                    task.projectOwner?.let {
                        setProject(it)
                    }
                    task.milestoneId?.let {
                        viewModelScope.launch {
                            getMilestoneById(task.milestoneId!!).collectLatest { milestone ->
                                withContext(Dispatchers.Main) {
                                    _uiState.update {
                                        it.copy(milestone = milestone)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }


    fun setTitle(text: String) {
        _uiState.update { it.copy(title = text) }
        if (text.isNotEmpty())
            _uiState.update {
                it.copy(taskInputState = it.taskInputState.copy(isTitleEmpty = false))
            }
    }

    fun setDescription(text: String) {
        _uiState.update { it.copy(description = text) }
    }

    fun setPriority(priority: TaskPriority) {
        _uiState.update { it.copy(priority = priority) }
    }

    fun setStringForFilteringProjects(value: String) {
        _uiState.update { it.copy(projectSearchQuery = value) }
        getAllProjects.setFilter(searchQuery = value)
    }

    fun setProject(project: Project) {
        viewModelScope.launch {
            getProjectById(project.id).collectLatest { project ->
                _uiState.update { it.copy(project = project) }
            }
        }
        viewModelScope.launch {
            getMilestonesForProject(project.id).collectLatest {
                getMilestonesForProject(project.id).collectLatest { milestones ->
                    _uiState.update { it.copy(milestonesList = milestones) }
                }
            }
            _uiState.update {
                it.copy(
                    taskInputState = it.taskInputState.copy(
                        isProjectEmpty = false
                    )
                )
            }
            //_taskInputState.value = _taskInputState.value.copy(isProjectEmpty = false)
        }
    }

    fun setMilestone(milestone: Milestone) {
        _uiState.update { it.copy(milestone = milestone) }
    }

    fun setTaskStatus(status: TaskStatus) {
        _uiState.update { it.copy(taskStatus = status) }
    }

    fun setDate(date: Date) {
        Log.d("setDate", date.toString())
        _uiState.update { it.copy(endDate = date) }
    }

    fun clearInput() {
        _uiState.value = TaskCreateState()
        taskId = null
    }

    fun addOrRemoveUser(user: User) {
        val listIds =
            uiState.value.chosenUserList?.getListUserIdsFromList()
        if (listIds?.contains(user.id) == true) {
            uiState.value.chosenUserList?.remove(
                uiState.value.chosenUserList!!.getUserById(
                    user.id
                )
            )
        } else {
            uiState.value.chosenUserList?.add(user)
        }
    }

    fun setUserSearch(query: String) {
        userSearch.value =
            UserFilter(query)
        _uiState.update { it.copy(userSearchQuery = query) }
    }


    fun createTask() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("createTask", "start creating a task")
            val response = createTask(
                uiState.value.milestone?.id,
                constructTask()
            )
            _uiState.update {
                it.copy(
                    taskInputState = TaskInputState(
                        serverResponse = response
                    )
                )
            }

        }
    }

    fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = updateTask(
                taskId,
                constructTask(),
                uiState.value.taskStatus
            )
            _uiState.update {
                it.copy(
                    taskInputState = TaskInputState(
                        serverResponse = response
                    )
                )
            }

        }
    }


    fun deleteTask() {
        Log.d("deleteTask", "start deleting a task")
        CoroutineScope(Dispatchers.IO).launch {
            val response = deleteTask(taskId)
            _uiState.update { it.copy(projectDeletionStatus = response) }
        }
    }

    private fun validateInput(
        onSuccess: () -> Unit
    ) {
        when {
            _uiState.value.title.isEmpty() -> _uiState.update {
                it.copy(
                    taskInputState = it.taskInputState.copy(
                        isTitleEmpty = true
                    )
                )
            }
            _uiState.value.chosenUserList?.isEmpty() == true -> _uiState.update {
                it.copy(
                    taskInputState = it.taskInputState.copy(isTeamEmpty = true)
                )
            }

            _uiState.value.project == null -> _uiState.update {
                it.copy(
                    taskInputState = it.taskInputState.copy(isProjectEmpty = true)
                )
            }

            else -> onSuccess()
        }
    }

    private fun constructTask()
            : Task {
        return Task(
            id = 0,
            description = uiState.value.description,
            deadline = uiState.value.endDate,
            title = uiState.value.title,
            projectOwner = uiState.value.project,
            responsibles = uiState.value.chosenUserList?.toMutableList()
                ?: mutableListOf(),
            priority = uiState.value.priority
        )
    }

}