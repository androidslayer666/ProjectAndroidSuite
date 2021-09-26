package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.MessageEntity
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
class MessageCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private var projectId: Int? = null

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private var _chosenUserList = MutableLiveData<MutableList<UserEntity>>(mutableListOf())
    val chosenUserList: LiveData<MutableList<UserEntity>> = _chosenUserList

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

//    fun setSubtask(subtask: SubtaskEntity) {
//        //Log.d("setTask", task.toString())
//        //taskId = subtask.id
//        _title.value = subtask.title
//        subtask.responsible?.let { _responsible.value = it }
//
//    }

    fun setTitle(string: String) {
        _title.value = string
    }

    fun setDescription(string: String) {
        _description.value = string
    }

    fun setProjectId(projectId: Int) {
        this.projectId = projectId
    }

    fun addOrRemoveUser(user: UserEntity) {
        //Log.d("addOrRemoveUser", user.toString())
        val listIds = _chosenUserList.value!!.getListIds()
        if (listIds.contains(user.id)) {
            _chosenUserList.value!!.remove(_chosenUserList.value!!.getUserById(user.id))
            //Log.d("addOrRemoveUser", task.value.toString())
        } else {
            _chosenUserList.value!!.add(user)
            //Log.d("addOrRemoveUser", task.value.toString())
        }
    }


    //todo only users
    fun clearInput() {
        _title.value = ""
        //_responsible.value = null
        _subtaskCreationStatus.value = null
        _subtaskUpdatingStatus.value = null
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }


    fun createMessage() {
        viewModelScope.launch(IO) {
            val response = messageRepository.putMessageToProject(
                //todo shouldNot be null
                projectId ?: 0,
                MessageEntity(
                    title = title.value ?: "",
                    id = 0,
                    description = description.value,
                ),
                chosenUserList.value?: listOf()
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _subtaskCreationStatus.value = response
            }
        }
    }

    fun updateMessage() {
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

