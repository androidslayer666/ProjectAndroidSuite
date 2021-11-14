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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private var _projectId = MutableStateFlow<Int?>(null)
    val projectId: StateFlow<Int?> = _projectId

    private var _currentProject = MutableStateFlow<Project?>(null)
    val currentProject: StateFlow<Project?> = _currentProject

    private var _listDiscussions = MutableStateFlow<List<Message>?>(null)
    val listDiscussions: StateFlow<List<Message>?> = _listDiscussions

    private var _taskAndMilestones = MutableStateFlow<Map<Milestone?, List<Task>>?>(null)
    val taskAndMilestones: StateFlow<Map<Milestone?, List<Task>>?> = _taskAndMilestones

    private var _listFiles = MutableStateFlow<List<File>?>(null)
    val listFiles: StateFlow<List<File>?> = _listFiles

    private var _projectDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val projectDeletionStatus: StateFlow<Result<String, String>?> = _projectDeletionStatus

    private var _messageDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val messageDeletionStatus: StateFlow<Result<String, String>?> = _messageDeletionStatus

    private var _commentDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val commentDeletionStatus: StateFlow<Result<String, String>?> = _commentDeletionStatus

    private var _milestoneDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val milestoneDeletionStatus: StateFlow<Result<String, String>?> = _milestoneDeletionStatus

    fun setProject(projectId: Int) {
        _projectId.value = projectId
        viewModelScope.launch {
            getProjectById(projectId).collectLatest {
                _currentProject.value = it
            }
        }
        viewModelScope.launch {
            getMessageByProjectId(projectId).collectLatest {
                //Log.d("getMessageByProjectId", it.toString())
                _listDiscussions.value = it
            }
        }
        viewModelScope.launch {
            getTaskAndMilestonesForProject(projectId).collectLatest {
                //Log.d("getTaskAndMilestones", it.toString())
                _taskAndMilestones.value = it
            }
        }
        viewModelScope.launch {
            getFilesByProjectId(projectId).collectLatest {
                //Log.d("getFilesByProjectId", it.toString())
                _listFiles.value = it
            }
        }
    }

    fun deleteProject() {
        if (projectId.value != null) {
            CoroutineScope(IO).launch {
                _projectDeletionStatus.value = deleteProject(projectId.value!!)
            }
        }
    }

    fun addCommentToMessage(comment: Comment) {
        CoroutineScope(IO).launch {
            putCommentToMessage(comment.messageId, comment, projectId.value)
        }
    }

    fun deleteMilestone(milestoneEntity: Milestone?) {
        CoroutineScope(IO).launch {
            _milestoneDeletionStatus.value = deleteMilestone(milestoneEntity?.id, projectId.value)
        }
    }

    fun resetState() {
        _projectDeletionStatus.value = null
    }

    fun deleteComment(commentEntity: Comment) {
        CoroutineScope(IO).launch {
            _commentDeletionStatus.value = deleteComment(commentEntity.id)
        }
    }

    fun deleteMessage(message: Message) {
        CoroutineScope(IO).launch {
            _messageDeletionStatus.value = deleteMessage(message.id, projectId.value)
        }
    }
}