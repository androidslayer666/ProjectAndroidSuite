package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.interactor.comment.PutCommentToMessage
import com.example.domain.interactor.files.GetFilesByProjectId
import com.example.domain.interactor.message.DeleteMessage
import com.example.domain.interactor.message.GetMessageByProjectId
import com.example.domain.interactor.milestone.DeleteMilestone
import com.example.domain.interactor.milestone.GetTaskAndMilestonesForProject
import com.example.domain.interactor.project.DeleteProject
import com.example.domain.interactor.project.GetProjectById
import com.example.domain.model.*
import com.example.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.transform
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

    val currentProject = _projectId.transform <Int?, Project> { projectId ->
        getProjectById(projectId)
    }

    val listDiscussions = _projectId.transform <Int?, List<Message>> { projectId ->
        getMessageByProjectId(projectId)
    }

    val listFiles = _projectId.transform<Int?, List<File>> { projectId ->
        getFilesByProjectId(projectId)
    }

    private var _projectDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val projectDeletionStatus: StateFlow<Result<String, String>?> = _projectDeletionStatus

    private var _messageDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val messageDeletionStatus: StateFlow<Result<String, String>?> = _messageDeletionStatus

    private var _commentDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val commentDeletionStatus: StateFlow<Result<String, String>?> = _commentDeletionStatus

    private var _milestoneDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val milestoneDeletionStatus: StateFlow<Result<String, String>?> = _milestoneDeletionStatus

    val taskAndMilestones =
        _projectId.transform<Int?, Map<Milestone?, List<Task>>> { projectId ->
            getTaskAndMilestonesForProject(projectId?:0)
        }

    fun setProject(projectId: Int) {
        _projectId.value = projectId
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
            _messageDeletionStatus.value = deleteMessage(message.id)
        }
    }
}