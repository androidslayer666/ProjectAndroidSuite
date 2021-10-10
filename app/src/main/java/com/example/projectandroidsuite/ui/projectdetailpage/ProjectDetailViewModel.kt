package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.*
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
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val milestoneRepository: MilestoneRepository,
    private val messageRepository: MessageRepository,
    private val fileRepository: FileRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private var _projectId = MutableLiveData<Int>()
    val projectId: LiveData<Int> = _projectId

    val currentProject = _projectId.switchMap { projectId ->
        projectRepository.getProjectFromDbById(projectId).asLiveData()
    }

    val listDiscussions = _projectId.switchMap { projectId ->
        messageRepository.getMessagesByProjectId(projectId).asLiveData()
    }

    val listFiles: LiveData<List<File>> = _projectId.switchMap { projectId ->
        fileRepository.getFilesWithProjectId(projectId).asLiveData()
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
            taskRepository.getTasksByProject(projectId)
                .combine(milestoneRepository.getMilestonesByProjectFlow(projectId))
                { taskList, milestoneList ->
                    return@combine arrangeMilestonesAndTasks(milestoneList, taskList)
                }.asLiveData()
        }

    fun setProject(projectId: Int) {
        _projectId.value = projectId
        viewModelScope.launch(IO) {
            messageRepository.populateMessageWithProjectId(projectId)
            fileRepository.populateProjectFiles(projectId)
            milestoneRepository.populateMilestonesByProject(projectId)
            taskRepository.populateTasksByProject(projectId)
        }
    }

    fun deleteProject() {
        if (projectId.value != null) {
            CoroutineScope(IO).launch {
                val result = projectRepository.deleteProject(projectId.value!!)
                if(_projectDeletionStatus.value == null) {
                    withContext(Main) {
                        _projectDeletionStatus.value = result
                    }
                }
            }
        }
    }

    //todo
    fun addCommentToMessage(comment: Comment){
        Log.d("ProjectDetailViewModel", comment.toString())
        CoroutineScope(IO).launch {
            val result = commentRepository.putCommentToMessage(comment.messageId ?:0, comment)
            if (result is Success) {
                //Log.d("ProjectDetailViewModel", projectId.value.toString())
                projectId.value?.let { messageRepository.populateMessageWithProjectId(it) }
            }
        }
    }

    fun deleteMilestone(milestoneEntity: Milestone?) {
        CoroutineScope(IO).launch {
            if(milestoneEntity != null){
                val response = milestoneRepository.deleteMilestone(milestoneEntity.id, projectId.value)
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
            val response = commentRepository.deleteComment(commentEntity.id)
            if(projectId.value != null) {
                messageRepository.populateMessageWithProjectId(projectId.value!!)
                withContext(Main){
                    _commentDeletionStatus.value = response
                }
            }
        }
    }

    fun deleteMessage(message: Message) {
        CoroutineScope(IO).launch {
            val response = messageRepository.deleteMessage(message.id)
            if(projectId.value != null) {
                messageRepository.populateMessageWithProjectId(projectId.value!!)
                withContext(Main){
                    _messageDeletionStatus.value = response
                }
            }
        }
    }
}