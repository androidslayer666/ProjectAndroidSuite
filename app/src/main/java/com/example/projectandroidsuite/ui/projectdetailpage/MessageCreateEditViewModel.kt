package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.filters.user.UserFilter
import com.example.domain.interactor.message.CreateMessage
import com.example.domain.interactor.message.UpdateMessage
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.utils.Result
import com.example.domain.utils.getListUserIdsFromList
import com.example.projectandroidsuite.ui.utils.getUserById
import com.example.projectandroidsuite.ui.utils.validation.MessageInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createMessage: CreateMessage,
    private val updateMessage: UpdateMessage
) : ViewModel() {

    private var projectId: Int? = null

    private var messageId: Int? = null

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private var _chosenUserList = MutableStateFlow<MutableList<User>>(mutableListOf())
    val chosenUserList: StateFlow<List<User>> = _chosenUserList

    private var userSearch = MutableStateFlow<UserFilter?>(null)

    private var _userSearchQuery = MutableStateFlow("")
    val userSearchQuery: StateFlow<String> = _userSearchQuery

    private var _creationStatus = MutableStateFlow<Result<String, String>?>(null)
    val creationStatus: StateFlow<Result<String, String>?> = _creationStatus

    private var _updatingStatus = MutableStateFlow<Result<String, String>?>(null)
    val updatingStatus: StateFlow<Result<String, String>?> = _updatingStatus

    private var _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    private var _messageInputState = MutableStateFlow(MessageInputState())
    val messageInputState: StateFlow<MessageInputState> = _messageInputState

    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { listUsers ->
                _users.value = listUsers
            }
        }
    }

    fun setMessage(message: Message) {
        messageId = message.id
        _title.value = message.title
        _content.value = message.text
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

    fun updateChosenUsers(){
        getAllUsers.setChosenUsersList(_chosenUserList.value)
    }

    fun addOrRemoveUser(user: User) {
        val listIds = _chosenUserList.value.getListUserIdsFromList()
        if (listIds.contains(user.id)) {
            _chosenUserList.value.remove(_chosenUserList.value.getUserById(user.id))
        } else {
            _chosenUserList.value.add(user)
        }
    }

    fun clearInput() {
        projectId = null
        _title.value = ""
        _content.value = ""
        _chosenUserList.value = mutableListOf()
        _creationStatus.value = null
        _updatingStatus.value = null
        _messageInputState.value = MessageInputState()
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }

    fun createMessage() {
        validateInput {
            viewModelScope.launch(IO) {
                val response = createMessage(
                    projectId ?: 0,
                    Message(
                        title = title.value,
                        id = 0,
                        description = content.value,
                    ),
                    chosenUserList.value
                )
                _messageInputState.value = MessageInputState(serverResponse = response)
            }
        }
    }

    fun updateMessage() {
        validateInput {
            viewModelScope.launch(IO) {
                val response = updateMessage(
                    projectId ?: 0,
                    Message(
                        title = title.value,
                        id = messageId ?: 0,
                        description = content.value,
                    ),
                    chosenUserList.value
                )
                _messageInputState.value = MessageInputState(serverResponse = response)            }
        }
    }
    private fun validateInput (onSuccess: ()->Unit) {
        when{
            title.value.isEmpty() -> _messageInputState.value = _messageInputState.value.copy(isTitleEmpty = true)
            chosenUserList.value.size < 1 -> _messageInputState.value = _messageInputState.value.copy(isTeamEmpty = true)
            content.value.isEmpty() -> _messageInputState.value = _messageInputState.value.copy(isTextEmpty =  true)
            else ->  onSuccess()
        }
    }
}

