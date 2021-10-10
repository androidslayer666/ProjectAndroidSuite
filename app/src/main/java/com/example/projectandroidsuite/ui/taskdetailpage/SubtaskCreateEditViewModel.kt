package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.SubtaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.model.Subtask
import com.example.domain.model.User
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.Result
import com.example.domain.repository.TaskRepository
import com.example.domain.repository.TeamRepository
import com.example.projectandroidsuite.logic.UserFilter
import com.example.projectandroidsuite.logic.combineWith
import com.example.projectandroidsuite.logic.filterUsersByFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubtaskCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private var taskId: Int? = null

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _responsible = MutableLiveData<User?>()
    val responsible: LiveData<User?> = _responsible

    private var userSearch = MutableLiveData<UserFilter>()

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchQuery: LiveData<String> = _userSearchQuery

    private var _subtaskCreationStatus = MutableLiveData<Result<String, String>?>()
    val subtaskCreationStatus: LiveData<Result<String, String>?> = _subtaskCreationStatus

    private var _subtaskUpdatingStatus = MutableLiveData<Result<String, String>?>()
    val subtaskUpdatingStatus: LiveData<Result<String, String>?> = _subtaskUpdatingStatus

    val userListFlow = teamRepository.getAllPortalUsers().asLiveData()
        .combineWith(userSearch) { listProject, filter ->
            if (filter != null) listProject?.filterUsersByFilter(filter)
            else listProject
        }


    init {
        viewModelScope.launch(IO) {
            teamRepository.populateAllPortalUsers()
            projectRepository.getProjects()
        }
    }

    fun setSubtask(subtask: Subtask) {
        //Log.d("setTask", task.toString())
        taskId = subtask.id
        _title.value = subtask.title
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

    //todo only users
    fun clearInput() {
        _title.value = ""
        _responsible.value = null
        _subtaskCreationStatus.value = null
        _subtaskUpdatingStatus.value = null
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }

    fun createSubtask() {
        viewModelScope.launch(IO) {
            val response = taskRepository.createSubtask(
                Subtask(
                    id = 0,
                    title = title.value ?: "",
                    responsible = responsible.value,
                    taskId = taskId ?: 0
                )
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _subtaskCreationStatus.value = response
            }
        }
    }
}