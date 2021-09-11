package com.example.projectandroidsuite.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.*
import com.example.domain.mappers.toListUserEntity
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.FileRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.repository.TeamRepository
import com.example.network.dto.CommentDto
import com.example.network.dto.TaskDto
import com.example.network.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import java.security.AccessController.getContext
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val teamRepository: TeamRepository,
    private val fileRepository: FileRepository,
    private val commentRepository: CommentRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private var _currentTask = MutableLiveData<TaskEntity>()
    val currentTask: LiveData<TaskEntity> = _currentTask

    private var _team = MutableLiveData<MutableList<UserEntity>>()
    val team: LiveData<MutableList<UserEntity>> = _team

    private var _filesForTask = MutableLiveData<List<FileEntity>>()
    val filesForTask = fileRepository.getFilesWithTaskId(state.get("taskId") ?: 0).asLiveData()

    val commentsForTask =
        commentRepository.getCommentByTaskId(state.get("taskId") ?: 0).asLiveData()

    private var _listTaskAndMilestones = MutableLiveData<List<TaskOrMilestoneEntity>>()
    val listTaskAndMilestones: LiveData<List<TaskOrMilestoneEntity>> = _listTaskAndMilestones

    init {
        viewModelScope.launch(IO) {
            val taskId = state.get("taskId") ?: 0
            taskRepository.getTaskById(taskId).collectLatest {
                //Log.d("TaskDetailViewModel", it.toString())
                if (it?.responsibles != null) {
                    val team = it.responsibles
                    //val files = fileRepository.getTaskFiles(taskId)
                    withContext(Dispatchers.Main) {
                        _currentTask.value = it
                        if (team != null) _team.value = team.toMutableList()
                    }
                }
            }
            commentRepository.populateCommentsWithTaskId(taskId)
            fileRepository.getTaskFiles(taskId)
            //Log.d("TaskDetailViewModel", taskId.toString())
        }
    }

    fun dismissTeamMember(user: UserEntity) {
        //Log.d("dismissTeamMember", user.toString())
        val team = _team.value!!
        team.remove(user)
        _team.value = team
    }

    //
    fun deleteTask(task: TaskEntity) {
        CoroutineScope(IO).launch { taskRepository.deleteTask(task) }

    }
}