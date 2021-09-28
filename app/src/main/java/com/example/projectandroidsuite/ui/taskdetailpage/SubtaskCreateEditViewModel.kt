package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.ProjectEntity
import com.example.database.entities.SubtaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.*
import com.example.network.dto.SubtaskPost
import com.example.network.dto.TaskPost
import com.example.projectandroidsuite.logic.Constants.FORMAT_API_DATE
import com.example.projectandroidsuite.logic.*

import com.example.projectandroidsuite.ui.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
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

    private var _responsible = MutableLiveData<UserEntity?>()
    val responsible: LiveData<UserEntity?> = _responsible

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
            //if(projectResponse is Failure<String>) _problemWithFetchingProjects.value = projectResponse.reason!!
        }
    }

    fun setSubtask(subtask: SubtaskEntity) {
        //Log.d("setTask", task.toString())
        taskId = subtask.id
        _title.value = subtask.title
        subtask.responsible?.let { _responsible.value = it }

    }

    fun setTitle(string: String) {
        _title.value = string
    }

    fun setTaskId(taskId: Int){
        this.taskId = taskId
    }


    fun setResponsible(user: UserEntity) {
        //Log.d("ProjectCreateEditodel", "setting the manager" + user.toString())
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
                //todo shouldNot be null
                taskId ?: 0,
                SubtaskPost(
                    title = title.value ?: "",
                    responsible = responsible.value?.id ?: ""
                )
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _subtaskCreationStatus.value = response
            }
    }
}

fun updateSubtask() {
    viewModelScope.launch(IO) {
//        val response = taskRepository.updateTask(
//            //todo shouldNot be null
//            taskId ?: 0,
//            TaskPost(
//                title = title.value,
//            )
//        )
//        withContext(Dispatchers.Main) {
//            _subtaskUpdatingStatus.value = response
//        }
    }
}
}

