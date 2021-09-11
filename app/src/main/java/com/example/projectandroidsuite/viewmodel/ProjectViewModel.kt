package com.example.projectandroidsuite.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.SessionManager
import com.example.domain.TestRepository
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.network.dto.TaskDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    repository: ProjectRepository,
    testRepository: TestRepository,
    taskRepository: TaskRepository,
    sessionManager: SessionManager
) : ViewModel() {
    val projects = repository.projectsFromDb.asLiveData()


    val allTasks = taskRepository.getAllUserTasks().asLiveData()




    init {
        viewModelScope.launch(IO) {
            taskRepository.populateTasks()
            repository.getProjects()
        }
    }

}