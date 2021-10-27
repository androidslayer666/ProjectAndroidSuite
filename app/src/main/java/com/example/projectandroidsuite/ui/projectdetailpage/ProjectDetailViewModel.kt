package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.entities.*
import com.example.domain.Result
import com.example.domain.Success
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
import com.example.domain.repository.*
import com.example.projectandroidsuite.logic.arrangeMilestonesAndTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
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

    private var _projectId = MutableLiveData<Int>()
    val projectId: LiveData<Int> = _projectId

    val currentProject = _projectId.switchMap { projectId ->
        getProjectById(projectId).asLiveData()
    }

    val listDiscussions = _projectId.switchMap { projectId ->
        getMessageByProjectId(projectId).asLiveData()
    }

    val listFiles: LiveData<List<File>> = _projectId.switchMap { projectId ->
        getFilesByProjectId(projectId).asLiveData()
    }

    private var _projectDeletionStatus = MutableLiveData<Result<String, String>?>()
    val projectDeletionStatus: LiveData<Result<String, String>?> = _projectDeletionStatus

    private var _messageDeletionStatus = MutableLiveData<Result<String, String>?>()
    val messageDeletionStatus: LiveData<Result<String, String>?> = _messageDeletionStatus

    private var _commentDeletionStatus = MutableLiveData<Result<String, String>?>()
    val commentDeletionStatus: LiveData<Result<String, String>?> = _commentDeletionStatus

    private var _milestoneDeletionStatus = MutableLiveData<Result<String, String>?>()
    val milestoneDeletionStatus: LiveData<Result<String, String>?> = _milestoneDeletionStatus

    val taskAndMilestones: LiveData<Map<Milestone?, List<Task>>> =
        _projectId.switchMap { projectId ->
            getTaskAndMilestonesForProject(projectId)
        }

    fun setProject(projectId: Int) {
        _projectId.value = projectId
    }

    fun deleteProject() {
        if (projectId.value != null) {
            CoroutineScope(IO).launch {
                val result = deleteProject(projectId.value!!)
                if(_projectDeletionStatus.value == null) {
                    withContext(Main) {
                        _projectDeletionStatus.value = result
                    }
                }
            }
        }
    }

    fun addCommentToMessage(comment: Comment){
        Log.d("ProjectDetailViewModel", comment.toString())
        CoroutineScope(IO).launch {
            val result = putCommentToMessage(comment.messageId, comment, projectId.value)
            if (result is Success) {
                //Log.d("ProjectDetailViewModel", projectId.value.toString())
                //projectId.value?.let { messageRepository.populateMessageWithProjectId(it) }
            }
        }
    }

    fun deleteMilestone(milestoneEntity: Milestone?) {
        CoroutineScope(IO).launch {
            if(milestoneEntity != null){
                val response = deleteMilestone(milestoneEntity.id, projectId.value)
                withContext(Main){
                    _milestoneDeletionStatus.value = response
                }
            }
        }
    }

    fun resetState(){
        _projectDeletionStatus.value = null
    }

    fun deleteComment(commentEntity: Comment){
        CoroutineScope(IO).launch {
            val response = deleteComment(commentEntity.id)
            if(projectId.value != null) {
                //messageRepository.populateMessageWithProjectId(projectId.value!!)
                withContext(Main){
                    _commentDeletionStatus.value = response
                }
            }
        }
    }

    fun deleteMessage(message: Message) {
        CoroutineScope(IO).launch {
            val response = deleteMessage(message.id)
            if(projectId.value != null) {
                //messageRepository.populateMessageWithProjectId(projectId.value!!)
                withContext(Main){
                    _messageDeletionStatus.value = response
                }
            }
        }
    }
}