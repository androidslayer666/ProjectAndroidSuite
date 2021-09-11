package com.example.projectandroidsuite.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.repository.TeamRepository
import com.example.network.dto.TaskPost
import com.example.projectandroidsuite.Constants.FORMAT_API_DATE

import com.example.projectandroidsuite.ui.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class TaskCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    private var _task = MutableLiveData<TaskEntity>()
    val task: LiveData<TaskEntity> = _task

    private var _userList = MutableLiveData<MutableList<UserEntity>>()
    val userList: LiveData<MutableList<UserEntity>> = _userList

    val projectList = projectRepository.getAllStoredProjects()
        .asLiveData() as MutableLiveData<List<ProjectEntity>>

    init {
        //Log.d("TaskCreateEditViewModel",state.keys().toString())

        viewModelScope.launch(IO) {
            teamRepository.populateAllPortalUsers()
            projectRepository.getProjects()

            withContext(Dispatchers.Main) {
                teamRepository.getAllPortalUsers().collectLatest {
                    _userList.value = it.toMutableList()
                }
            }
        }
    }

    fun setTask(task: TaskEntity) {
        //Log.d("setTask", task.toString())
        _task.value = task
    }

    fun clearChosenUsers() {
        if (userList.value != null) {
            for (user in userList.value!!) {
                user.chosen = null
            }
        }
    }

    fun chooseProject(project: ProjectEntity) {
        //Log.d("TaskCreateEditViewModel", "Now projectOwner is   ${project.title}")
        _task.value?.projectOwner = project
        _task.forceRefresh()
    }

    fun addOrRemoveUser(user: UserEntity) {
        //Log.d("addOrRemoveUser", user.toString())
        if (task.value?.responsibles != null) {
            val listIds = task.value?.responsibles?.getListIds()!!
            if (listIds.contains(user.id)) {
                task.value?.responsibles?.remove(task.value?.responsibles?.getUserById(user.id))
                //Log.d("addOrRemoveUser", task.value.toString())
                _task.forceRefresh()
            } else {
                task.value?.responsibles?.add(user)
                //Log.d("addOrRemoveUser", task.value.toString())
                _task.forceRefresh()
            }
        }
    }

    fun setDate(date: Date, setter: TaskDate) {
        when (setter) {
            TaskDate.STARTDATE -> _task.value?.created = date
            TaskDate.ENDDATE -> _task.value?.deadline = date
        }
    }

    fun setTitle(string: String) {
        _task.value?.title = string
    }

    fun setDescription(string: String) {
        _task.value?.description = string
    }

    fun createTask() {
        viewModelScope.launch(IO) {
            if (task.value?.projectOwner?.id != null) {
                taskRepository.createTask(
                    task.value?.projectOwner?.id!!,
                    TaskPost(
                        description = task.value?.description ?: "title",
                        deadline = SimpleDateFormat(FORMAT_API_DATE).format(task.value?.deadline ?: Date()),
                        priority = "normal",
                        title = task.value?.title ?: "title",
                        responsibles = task.value?.responsibles?.fromListUsersToStrings()
                            ?: listOf(),
                        notify = false,
                        startDate = SimpleDateFormat(FORMAT_API_DATE).format(task.value?.created ?: Date())
                    )
                )
            } else {

            }
        }
    }

    //todo Update
    fun updateTask(task: TaskEntity?) {
        taskRepository
    }
}

