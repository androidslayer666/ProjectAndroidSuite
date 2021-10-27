package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.Result
import com.example.domain.UserFilter
import com.example.domain.filterUsersByFilter
import com.example.domain.getListIds
import com.example.domain.interactor.message.CreateMessage
import com.example.domain.interactor.message.UpdateMessage
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.repository.*
import com.example.projectandroidsuite.logic.*

import com.example.projectandroidsuite.ui.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createMessage: CreateMessage,
    private val updateMessage: UpdateMessage
) : ViewModel() {

    private var projectId: Int? = null

    private var messageId: Int? = null

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _content = MutableLiveData("")
    val content: LiveData<String> = _content

    private var _chosenUserList = MutableLiveData<MutableList<User>>(mutableListOf())
    val chosenUserList: LiveData<MutableList<User>> = _chosenUserList

    private var userSearch = MutableLiveData<UserFilter>()

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchQuery: LiveData<String> = _userSearchQuery

    private var _creationStatus = MutableLiveData<Result<String, String>?>()
    val creationStatus: LiveData<Result<String, String>?> = _creationStatus

    private var _updatingStatus = MutableLiveData<Result<String, String>?>()
    val updatingStatus: LiveData<Result<String, String>?> = _updatingStatus

    private var _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    init {
        viewModelScope.launch(IO) {

            getAllUsers().asLiveData().combineWith(chosenUserList) { users, chosenUsers ->
                Log.d("ProjectCreateEditModel", chosenUsers?.map{it.displayName}.toString())
                users?.forEach { user ->
                    user.chosen = chosenUsers?.getListIds()?.contains(user.id) == true
                }
                return@combineWith users
            }
                .combineWith(userSearch) { listProject, filter ->
                    _users.value = listProject?.filterUsersByFilter(filter)
                }

        }
    }

    fun setMessage(message: Message) {
        messageId = message.id
        _title.value  = message.title
        _content.value = message.text
        //_chosenUserList.value = message.
    }


    fun setTitle(string: String) {
        _title.value = string
    }

    fun setContent(string: String) {
        _content.value = string
    }

    fun setProjectId(projectId: Int) {
        this.projectId = projectId
    }

    fun addOrRemoveUser(user: User) {
        //Log.d("addOrRemoveUser", user.toString())
        val listIds = _chosenUserList.value!!.getListIds()
        if (listIds.contains(user.id)) {
            _chosenUserList.value!!.remove(_chosenUserList.value!!.getUserById(user.id))
            _chosenUserList.forceRefresh()
            //Log.d("addOrRemoveUser", task.value.toString())
        } else {
            _chosenUserList.value!!.add(user)
            _chosenUserList.forceRefresh()
            //Log.d("addOrRemoveUser", task.value.toString())
        }
    }


    fun clearInput() {
        projectId = null
        _title.value = ""
        _content.value = ""
        _chosenUserList.value = mutableListOf()
        _creationStatus.value = null
        _updatingStatus.value = null
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }


    fun createMessage() {
        viewModelScope.launch(IO) {
            val response = createMessage(
                projectId ?: 0,
                Message(
                    title = title.value ?: "",
                    id = 0,
                    description = content.value,
                ),
                chosenUserList.value?: listOf()
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _creationStatus.value = response
            }
        }
    }

    fun updateMessage() {
        viewModelScope.launch(IO) {
        val response = updateMessage(
            projectId ?: 0,
                Message(
                    title = title.value ?: "",
                    id = messageId ?: 0,
                    description = content.value,
                ),
            chosenUserList.value?: listOf()
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _creationStatus.value = response
            }
        }
    }
}

