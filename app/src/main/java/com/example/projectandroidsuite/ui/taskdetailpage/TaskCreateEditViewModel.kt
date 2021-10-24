package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.Result
import com.example.domain.model.Milestone
import com.example.domain.model.Project
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.repository.*
import com.example.projectandroidsuite.logic.*
import com.example.projectandroidsuite.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private var taskId: Int? = null

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private var _priority = MutableLiveData(0)
    val priority: LiveData<Int> = _priority

    private var _chosenUserList = MutableLiveData<MutableList<User>>(mutableListOf())
    val chosenUserList: LiveData<MutableList<User>> = _chosenUserList

    private var _project = MutableLiveData<Project?>()
    val project: LiveData<Project?> = _project

    private var _milestone = MutableLiveData<Milestone?>()
    val milestone: LiveData<Milestone?> = _milestone

    private var projectSearch = MutableLiveData<ProjectFilter>()
    private var userSearch = MutableLiveData<UserFilter>()

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchQuery: LiveData<String> = _userSearchQuery

    private var _taskStatus = MutableLiveData<TaskStatus>()
    val taskStatus: LiveData<TaskStatus> = _taskStatus

    private var _projectSearchQuery = MutableLiveData<String>()
    val projectSearchQuery: LiveData<String> = _projectSearchQuery

    private var _taskCreationStatus = MutableLiveData<Result<String, String>?>()
    val taskCreationStatus: LiveData<Result<String, String>?> = _taskCreationStatus

    private var _taskUpdatingStatus = MutableLiveData<Result<String, String>?>()
    val taskUpdatingStatus: LiveData<Result<String, String>?> = _taskUpdatingStatus

    private var _endDate = MutableLiveData(Date())
    val endDate: LiveData<Date> = _endDate

    val userList = project.switchMap { project ->
        liveData { emit(project?.team) }
    }
        .combineWith(chosenUserList) { users, chosenUsers ->
            users?.forEach { user ->
                user.chosen = chosenUsers?.getListIds()?.contains(user.id) == true
            }
            return@combineWith users
        }
        .combineWith(userSearch) { users, filter ->
            if (filter != null) users?.filterUsersByFilter(filter)
            else users
        }


    val projectList = projectRepository.getAllStoredProjects().asLiveData()
        .combineWith(projectSearch) { listProject, filter ->
            if (filter != null) listProject?.filterProjectsByFilter(filter)
            else listProject
        }

    val milestonesList = project.switchMap {
        if (it?.id != null)
            milestoneRepository.getMilestonesByProjectFlow(it.id).asLiveData()
        else
            liveData { }
    }


    init {
        viewModelScope.launch(IO) {
            projectRepository.getProjects()
            teamRepository.populateAllPortalUsers()
            //if(projectResponse is Failure<String>) _problemWithFetchingProjects.value = projectResponse.reason!!
        }
    }

    fun setTask(task: Task) {
        //Log.d("setTask", task.toString())
        taskId = task.id
        _title.value = task.title
        _description.value = task.description
        _chosenUserList.value = task.responsibles
        _endDate.value = task.deadline
        _priority.value = task.priority
        task.projectOwner?.let { projectOwner ->
            viewModelScope.launch {
                projectRepository.getProjectFromDbById(projectOwner.id).collectLatest { project ->
                    _project.value = project
                }
            }
        }
        task.milestoneId?.let {
            viewModelScope.launch {
                val milestone = milestoneRepository.getMilestoneById(task.milestoneId!!)
                withContext(Main) {
                    _milestone.value = milestone
                }
            }
        }
    }

    fun setTitle(string: String) {
        _title.value = string
    }

    fun setDescription(string: String) {
        _description.value = string
    }

    fun setPriority(value: Int) {
        Log.d("setPriority", value.toString())
        _priority.value = value
    }

    fun setProject(project: Project) {
        _project.value = project
        Log.d("setProject", _project.value.toString())
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
        //Log.d("addOrRemoveUser", user.toString())
        val listIds = _chosenUserList.value!!.getListIds()
        if (listIds.contains(user.id)) {
            _chosenUserList.value!!.remove(_chosenUserList.value!!.getUserById(user.id))
            Log.d("addOrRemoveUser", "remove user $user")
            _chosenUserList.forceRefresh()
        } else {
            _chosenUserList.value!!.add(user)
            Log.d("addOrRemoveUser", "add user $user")
            _chosenUserList.forceRefresh()
        }
    }

    fun setProjectSearch(query: String) {
        _projectSearchQuery.value = query
        projectSearch.value =
            ProjectFilter(query, projectSearch.value?.responsible)
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }


    fun createTask() {
        viewModelScope.launch(IO) {

            if (project.value?.id != null) {
                val response = taskRepository.createTask(
                    milestone.value?.id ?: 0,
                    Task(
                        id = 0,
                        description = description.value?:"",
                        deadline = endDate.value?: Date(),
                        title = title.value?:"",
                        projectOwner= project.value,
                        responsibles = chosenUserList.value?: mutableListOf(),
                        priority = priority.value
                    )
                )
                withContext(Main) {
                    Log.d("TaskCreateEditViewModel", response.toString())
                    _taskCreationStatus.value = response
                }
            } else {
                Log.d("TaskCreateEditViewModel", "Project is null")
            }
        }
    }

    fun updateTask() {
        viewModelScope.launch(IO) {
            val response = taskRepository.updateTask(
                taskId ?: 0,
                Task(
                    id = 0,
                    description = description.value?:"",
                    deadline = endDate.value?: Date(),
                    title = title.value?:"",
                    projectOwner= project.value,
                    responsibles = chosenUserList.value?: mutableListOf(),
                    priority = priority.value
                ),
                when (taskStatus.value) {
                    TaskStatus.ACTIVE -> "Open"
                    TaskStatus.COMPLETE -> "Closed"
                    else -> "Open"
                }
            )
            withContext(Main) {
                Log.d("TaskCreateEditViewModel", response.toString())
                _taskUpdatingStatus.value = response
            }
        }
    }
}

