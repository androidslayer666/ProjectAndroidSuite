package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.domain.model.Milestone
import com.example.domain.model.Task
import com.example.domain.utils.Result
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

    private var _taskId = MutableStateFlow<Int?>(null)
    val taskId: StateFlow<Int?> = _taskId

    private var _currentTask = MutableStateFlow<Task?>(null)
    val currentTask: StateFlow<Task?> = _currentTask

    private var _taskMilestone = MutableStateFlow<Milestone?>(null)
    val taskMilestone: StateFlow<Milestone?> = _taskMilestone

    private var _filesForTask = MutableStateFlow<List<File>?>(null)
    val filesForTask: StateFlow<List<File>?> = _filesForTask

    private var _listComments = MutableStateFlow<List<Comment>?>(null)
    val listComments: StateFlow<List<Comment>?> = _listComments

    private var _taskDeletionStatus = MutableStateFlow<Result<String, String>?>(null)
    val taskDeletionStatus: StateFlow<Result<String, String>?> = _taskDeletionStatus

    fun setCurrentTask(taskId: Int) {
        Log.d("DetailViewModel", taskId.toString())
        _taskId.value = taskId
        viewModelScope.launch {
            getTaskById(taskId).collectLatest {
                Log.d("DetailViewModel", it.toString())
                _currentTask.value = it
            }
        }
        viewModelScope.launch {
            getCommentByTaskId(taskId).collectLatest {
                _listComments.value = it
            }
        }
        viewModelScope.launch {
            getFilesByTaskId(taskId).collectLatest {
                _filesForTask.value = it
            }
        }
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