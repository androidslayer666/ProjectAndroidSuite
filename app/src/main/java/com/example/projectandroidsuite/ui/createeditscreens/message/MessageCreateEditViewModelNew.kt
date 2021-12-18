package com.example.projectandroidsuite.ui.createeditscreens.message


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.message.CreateMessage
import com.example.domain.interactor.message.DeleteMessage
import com.example.domain.interactor.message.GetMessageById
import com.example.domain.interactor.message.UpdateMessage
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.utils.Result
import com.example.domain.utils.getListUserIdsFromList
import com.example.projectandroidsuite.ui.utils.getUserById
import com.example.projectandroidsuite.ui.utils.validation.MessageInputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageCreateState(
    val title: String = "",
    val content: String = "",
    val chosenUserList: MutableList<User> = mutableListOf(),
    val userSearchQuery: String = "",
    val users: List<User>? = null,
    val messageInputState: MessageInputState = MessageInputState(),
    val messageDeletionStatus: Result<String, String>? = null
)


@HiltViewModel
class MessageCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val createMessage: CreateMessage,
    private val getMessageById: GetMessageById,
    private val updateMessage: UpdateMessage,
    private val deleteMessage: DeleteMessage
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageCreateState())
    val uiState: StateFlow<MessageCreateState> = _uiState.asStateFlow()

    private var projectId: Int? = null

    private var messageId: Int? = null


    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { listUsers ->
                _uiState.update { it.copy(users = listUsers) }
            }
        }
    }

    fun setProjectId(projectId: Int) {
        this.projectId = projectId
    }

    fun setMessage(messageId: Int) {
        this.messageId = messageId
        viewModelScope.launch {

            getMessageById(messageId).collectLatest { message ->
                if (message != null) {
                    _uiState.update {
                        it.copy(
                            title = message.title,
                            content = message.text ?: "",
                            //todo add fetching team for message
                        )
                    }
                }
            }
        }
    }


    fun setTitle(text: String) {
        _uiState.update { it.copy(title = text) }
    }


    fun setContent(text: String) {
        _uiState.update { it.copy(content = text) }
    }


    fun updateChosenUsers() {
        getAllUsers.setChosenUsersList(uiState.value.chosenUserList)
    }

    fun addOrRemoveUser(user: User) {

        val listIds = uiState.value.chosenUserList.getListUserIdsFromList()
        if (listIds.contains(user.id)) {

            uiState.value.chosenUserList.remove(uiState.value.chosenUserList.getUserById(user.id))
        } else {

            uiState.value.chosenUserList.add(user)
        }
    }

    fun clearInput() {
        projectId = null

        messageId = null
        _uiState.value = MessageCreateState()
    }

    fun setUserSearch(query: String) {
        _uiState.update { it.copy(userSearchQuery = query) }
    }

    fun createMessage() {
        viewModelScope.launch(IO) {
            val response = createMessage(
                projectId ?: 0,
                constructMessage(),
                uiState.value.chosenUserList
            )

            _uiState.update { it.copy(messageInputState = MessageInputState(serverResponse = response)) }
        }
    }

    fun updateMessage() {
        viewModelScope.launch(IO) {
            val response = updateMessage(
                projectId ?: 0,

                constructMessage(),
                uiState.value.chosenUserList
            )
            _uiState.update { it.copy(messageInputState = MessageInputState(serverResponse = response)) }
        }
    }


    fun deleteMessage() {
        CoroutineScope(IO).launch {
            messageId?.let {
                val response = deleteMessage(messageId ?: 0, projectId)
                _uiState.update { it.copy(messageDeletionStatus = response) }
            }
        }
    }

    private fun validateInput(onSuccess: () -> Unit) {
        when {
            _uiState.value.title.isEmpty() -> _uiState.update {
                it.copy(
                    messageInputState = it.messageInputState.copy(
                        isTitleEmpty = true
                    )
                )
            }
            _uiState.value.chosenUserList.isEmpty() -> _uiState.update {
                it.copy(
                    messageInputState = it.messageInputState.copy(
                        isTeamEmpty = true
                    )
                )
            }
            _uiState.value.content.isEmpty() -> _uiState.update {
                it.copy(
                    messageInputState = it.messageInputState.copy(
                        isTextEmpty = true
                    )
                )
            }
            else -> onSuccess()
        }
    }

    private fun constructMessage(): Message {
        return Message(
            title = uiState.value.title,
            id = messageId ?: 0,
            text = uiState.value.content,
        )
    }

}

