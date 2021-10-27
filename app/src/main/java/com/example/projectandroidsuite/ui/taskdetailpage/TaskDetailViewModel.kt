package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.Result
import com.example.domain.Success
import com.example.domain.interactor.GetFilesByTaskId
import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.interactor.comment.GetCommentByTaskId
import com.example.domain.interactor.comment.PutCommentToTask
import com.example.domain.interactor.milestone.GetMilestoneById
import com.example.domain.interactor.task.DeleteTask
import com.example.domain.interactor.task.GetTaskById
import com.example.domain.interactor.task.UpdateTaskStatus
import com.example.domain.model.Comment
import com.example.domain.model.File
import com.example.domain.model.Task
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.MilestoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskById: GetTaskById,
    private val deleteTask: DeleteTask,
    private val updateTaskStatus: UpdateTaskStatus,
    private val getFilesByTaskId: GetFilesByTaskId,
    private val getCommentByTaskId: GetCommentByTaskId,
    private val getMilestoneById: GetMilestoneById,
    private val putCommentToTask: PutCommentToTask,
    private val deleteComment: DeleteComment
) : ViewModel() {

    private var _taskId = MutableLiveData<Int?>(null)
    val taskId: LiveData<Int?> = _taskId

    val currentTask = _taskId.switchMap <Int?, Task?> { taskId ->
        getTaskById(taskId?:0).asLiveData()
    }

    val taskMilestone = currentTask.switchMap { task ->
        getMilestoneById(task?.milestoneId).asLiveData()
    }

    val filesForTask = _taskId.switchMap<Int?, List<File>> { taskId ->
        getFilesByTaskId(taskId ?: 0).asLiveData()
    }

    val listComments = _taskId.asFlow().transform<Int?, List<Comment>?> { taskId ->
        getCommentByTaskId(taskId).collect { emit(it) }
    }

    private var _taskDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val taskDeletionStatus: StateFlow<Result<String, String>?> = _taskDeletionStatus

    fun setCurrentTask(taskId: Int) {
        _taskId.value = taskId
    }

    fun deleteTask() {
        CoroutineScope(IO).launch {
            _taskDeletionStatus.value = deleteTask(taskId.value)
        }
    }

    fun addCommentToTask(comment: Comment) {
        CoroutineScope(IO).launch {
            putCommentToTask(taskId.value ?: 0, comment)
        }
    }

    fun resetState() {
        _taskDeletionStatus.value = null
    }

    fun deleteComment(commentEntity: Comment) {
        CoroutineScope(IO).launch {
            deleteComment(commentEntity.id, taskId.value)
        }
    }

    fun changeTaskStatus() {
        viewModelScope.launch(IO) {
            updateTaskStatus(taskId.value, currentTask.value?.status)
        }
    }
}