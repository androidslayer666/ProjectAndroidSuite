package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.interactor.comment.PutCommentToMessage
import com.example.domain.interactor.file.GetFilesByProjectId
import com.example.domain.interactor.message.DeleteMessage
import com.example.domain.interactor.message.GetMessageByProjectId
import com.example.domain.interactor.milestone.DeleteMilestone
import com.example.domain.interactor.milestone.GetTaskAndMilestonesForProject
import com.example.domain.interactor.project.DeleteProject
import com.example.domain.interactor.project.GetProjectById
import com.example.domain.model.*
import com.example.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectDetailScreenState(
    val projectId: Int? = null,
    val currentProject: Project? = null,
    val messages: List<Message>? = null,
    val userSearchProject: String = "",
    val taskAndMilestones: Map<Milestone?, List<Task>>? = emptyMap(),
    val listFiles: List<File>? = emptyList(),
    val projectDeletionStatus: Result<String, String>? = null,
    val messageDeletionStatus: Result<String, String>? = null,
    val commentDeletionStatus: Result<String, String>? = null,
    val milestoneDeletionStatus: Result<String, String>? = null,
)

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val getProjectById: GetProjectById,
    private val deleteProject: DeleteProject,
    private val getTaskAndMilestonesForProject: GetTaskAndMilestonesForProject,
    private val deleteMilestone: DeleteMilestone,
    private val getMessageByProjectId: GetMessageByProjectId,
    private val deleteMessage: DeleteMessage,
    private val getFilesByProjectId: GetFilesByProjectId,
    private val deleteComment: DeleteComment,
    private val putCommentToMessage: PutCommentToMessage
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectDetailScreenState())
    val uiState: StateFlow<ProjectDetailScreenState> = _uiState.asStateFlow()

    fun setProject(projectId: Int) {
        _uiState.update { it.copy(projectId = projectId) }
        viewModelScope.launch {
            getProjectById(projectId).collectLatest { project ->
                _uiState.update { it.copy(currentProject = project) }
            }
        }
        viewModelScope.launch {
            getMessageByProjectId(projectId).collectLatest { list ->
                //Log.d("getMessageByProjectId", it.toString())
                _uiState.update { it.copy(messages = list) }
            }
        }
        viewModelScope.launch {
            getTaskAndMilestonesForProject(projectId).collectLatest { taskAndMilestones ->
                //Log.d("getTaskAndMilestones", it.toString())
                _uiState.update { it.copy(taskAndMilestones = taskAndMilestones) }
            }
        }
        viewModelScope.launch {
            getFilesByProjectId(projectId).collectLatest { files ->
                //Log.d("getFilesByProjectId", it.toString())
                _uiState.update { it.copy(listFiles = files) }
            }
        }
    }

    fun deleteProject() {
        uiState.value.projectId?.let{
            CoroutineScope(IO).launch {
                val response = deleteProject(uiState.value.projectId!!)
                _uiState.update { it.copy(projectDeletionStatus = response) }
            }
        }
    }

    fun addCommentToMessage(comment: Comment) {
        CoroutineScope(IO).launch {
            putCommentToMessage(comment.messageId, comment, uiState.value.projectId)
        }
    }

    fun deleteMilestone(milestoneEntity: Milestone?) {
        CoroutineScope(IO).launch {
            val response = deleteMilestone(milestoneEntity?.id, uiState.value.projectId)
            _uiState.update { it.copy(projectDeletionStatus = response) }
        }
    }

    fun resetState() {
        _uiState.value = ProjectDetailScreenState()
    }

    fun deleteComment(commentEntity: Comment) {
        CoroutineScope(IO).launch {
            val response = deleteComment(commentEntity.id)
            _uiState.update { it.copy(projectDeletionStatus = response) }
        }
    }

    fun deleteMessage(message: Message) {
        CoroutineScope(IO).launch {
            val status = deleteMessage(message.id, uiState.value.projectId)
            _uiState.update { it.copy(projectDeletionStatus = status) }
        }
    }
}