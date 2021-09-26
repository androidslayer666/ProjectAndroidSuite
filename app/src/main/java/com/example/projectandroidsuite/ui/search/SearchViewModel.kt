package com.example.projectandroidsuite.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.domain.repository.FileRepository
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.projectandroidsuite.logic.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    projectRepository: ProjectRepository,
    taskRepository: TaskRepository,
    private val milestoneRepository: MilestoneRepository,
    private val fileRepository: FileRepository
) : ViewModel() {

    private var _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    val projects = projectRepository.getAllStoredProjects().asLiveData()
        .combineWith(searchString) { listProjects, searchString ->
            if (!searchString.isNullOrEmpty()) {
                listProjects?.filterProjectsByFilter(ProjectFilter(searchQuery = searchString))
            } else {
                listProjects
            }
        }


    val tasks = taskRepository.getAllUserTasks().asLiveData()
        .combineWith(searchString) { listTasks, searchString ->
            if (!searchString.isNullOrEmpty()) {
                listTasks?.filterTaskByFilter(TaskFilter(searchQuery = searchString))
            } else {
                listTasks
            }
        }

    fun setSearchString(searchString: String) {
        _searchString.value = searchString
    }

}