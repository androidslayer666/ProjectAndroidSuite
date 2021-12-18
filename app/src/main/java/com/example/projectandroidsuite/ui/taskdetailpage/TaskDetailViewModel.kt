package com.example.projectandroidsuite.ui.taskdetailpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.interactor.comment.GetCommentByTaskId
import com.example.domain.interactor.comment.PutCommentToTask
import com.example.domain.interactor.file.GetFilesByTaskId
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

data class TaskDetailScreenState(
    val taskId: Int? = null,
    val currentTask: Task? = null,
    val taskAndMilestones: Map<Milestone?, List<Task>>? = emptyMap(),
    val listFiles: List<File>? = emptyList(),
    val listComments: List<Comment>? = null,
    val taskDeletionStatus: Result<String, String>? = null
)

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskById: GetTaskById,
    private val deleteTask: DeleteTask,
    private val updateTaskStatus: UpdateTaskStatus,
    private val getFilesByTaskId: GetFilesByTaskId,
    private val getCommentByTaskId: GetCommentByTaskId,
    private val putCommentToTask: PutCommentToTask,
    private val deleteComment: DeleteComment
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailScreenState())
    val uiState: StateFlow<TaskDetailScreenState> = _uiState.asStateFlow()

    fun setCurrentTask(taskId: Int) {
        Log.d("DetailViewModel", taskId.toString())
        _uiState.update { it.copy(taskId = taskId) }
        viewModelScope.launch {
            getTaskById(taskId).collectLatest { task ->
                _uiState.update { it.copy(currentTask = task) }
            }
        }
        viewModelScope.launch {
            getCommentByTaskId(taskId).collectLatest { list->
                _uiState.update { it.copy(listComments = list) }
            }
        }
        viewModelScope.launch {
            getFilesByTaskId(taskId).collectLatest {list->
                _uiState.update { it.copy(listFiles = list) }
            }
        }
    }

    fun deleteTask() {
        CoroutineScope(IO).launch {
            val response = deleteTask(uiState.value.taskId)
            _uiState.update { it.copy(taskDeletionStatus = response) }
        }
    }

    fun addCommentToTask(comment: Comment) {
        CoroutineScope(IO).launch {
            putCommentToTask(uiState.value.taskId ?: 0, comment)
        }
    }

    fun resetState() {
        _uiState.value = TaskDetailScreenState()
    }

    fun deleteComment(commentEntity: Comment) {
        CoroutineScope(IO).launch {
            deleteComment(commentEntity.id, uiState.value.taskId)
        }
    }

    fun changeTaskStatus() {
        viewModelScope.launch(IO) {
            updateTaskStatus(uiState.value.taskId, uiState.value.currentTask?.status)
        }
    }
}