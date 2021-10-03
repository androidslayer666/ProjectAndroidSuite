package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.CommentEntity
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.domain.repository.*
import com.example.network.dto.SubtaskPost
import com.example.network.dto.TaskPost
import com.example.projectandroidsuite.logic.Constants
import com.example.projectandroidsuite.logic.TaskStatus
import com.example.projectandroidsuite.logic.priorityToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val fileRepository: FileRepository,
    private val commentRepository: CommentRepository,
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private var _taskId = MutableLiveData<Int>()
    val taskId: LiveData<Int> = _taskId

    val currentTask = _taskId.switchMap { taskId ->
        taskRepository.getTaskById(taskId).asLiveData()
    }

    val taskMilestone = currentTask.switchMap { task ->
        if (task?.milestoneId != null) {
            liveData<MilestoneEntity> { emit(milestoneRepository.getMilestoneById(task.milestoneId!!)) }
        } else liveData {}
    }

    val filesForTask = _taskId.switchMap { taskId ->
        fileRepository.getFilesWithTaskId(taskId).asLiveData()
    }

    val listComments = _taskId.switchMap {
        commentRepository.getCommentByTaskId(it).asLiveData()
    }

    private var _taskDeletionStatus = MutableLiveData<Result<String, String>?>()
    val taskDeletionStatus: LiveData<Result<String, String>?> = _taskDeletionStatus


    fun setCurrentTask(taskId: Int) {
        _taskId.value = taskId
        viewModelScope.launch(IO) {
            //Log.d("TaskDetailViewModel", "Populating tasks with task ID" + taskId.toString())
            commentRepository.populateCommentsWithTaskId(taskId)
            fileRepository.populateTaskFiles(taskId)
        }
    }

    fun deleteTask() {
        taskId.value?.let {
            CoroutineScope(IO).launch {
                val result = taskRepository.deleteTask(it)
                withContext(Main) {
                    if (_taskDeletionStatus.value == null) {
                        _taskDeletionStatus.value = result
                    }
                }
            }
        }
    }

    fun addCommentToTask(comment: CommentEntity) {
        //Log.d("ProjectDetailViewModel", comment.toString())
        CoroutineScope(IO).launch {
            val result = commentRepository.putCommentToTask(taskId.value ?: 0, comment)
            if (result is Success) {
                //Log.d("ProjectDetailViewModel", projectId.value.toString())
                taskId.value?.let { commentRepository.populateCommentsWithTaskId(it) }
            }
        }
    }

    fun addSubtaskToTask(subtask: SubtaskPost) {
        CoroutineScope(IO).launch {
            val result = taskRepository.createSubtask(taskId.value ?: 0, subtask)
            if (result is Success) {
                //Log.d("ProjectDetailViewModel", projectId.value.toString())
                taskId.value?.let { commentRepository.populateCommentsWithTaskId(it) }
            }
        }
    }

    fun resetState() {
        _taskDeletionStatus.value = null
    }

    fun deleteComment(commentEntity: CommentEntity) {
        CoroutineScope(IO).launch {
            commentRepository.deleteComment(commentEntity.id, taskId.value ?: 0)
        }
    }

    fun changeTaskStatus() {
        viewModelScope.launch(IO) {
            Log.d("ProjectDetailViewModel", taskId.value.toString())
            Log.d("ProjectDetailViewModel", currentTask.value?.status.toString())
            if (taskId.value != null && currentTask.value?.status != null) {
                taskRepository.updateTaskStatus(
                    taskId.value!!,
                    when (currentTask.value?.status) {
                        1 -> "closed"
                        2 -> "open"
                        else -> "open"
                    }
                )
            }
        }
    }
}