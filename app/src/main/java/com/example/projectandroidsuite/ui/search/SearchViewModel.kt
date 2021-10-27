package com.example.projectandroidsuite.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.example.domain.*
import com.example.domain.interactor.files.GetAllFiles
import com.example.domain.interactor.milestone.GetAllMilestones
import com.example.domain.interactor.project.GetAllProjects
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.model.File
import com.example.domain.model.Milestone
import com.example.domain.model.Project
import com.example.domain.model.Task
import com.example.domain.repository.FileRepository
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.projectandroidsuite.logic.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAllProjects: GetAllProjects,
    private val getAllTasks: GetAllTasks,
    private val getAllMilestones: GetAllMilestones,
    private val getAllFiles: GetAllFiles,
) : ViewModel() {

    private var _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    private var _projects = MutableStateFlow<List<Project>>(listOf())
    val projects: StateFlow<List<Project>> = _projects

    private var _tasks = MutableStateFlow<List<Task>>(listOf())
    val tasks: StateFlow<List<Task>> = _tasks

    private var _milestones = MutableStateFlow<List<Milestone>>(listOf())
    val milestones: StateFlow<List<Milestone>> = _milestones

    private var _files = MutableStateFlow<List<File>>(listOf())
    val files: StateFlow<List<File>> = _files

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllProjects().collectLatest { _projects.value = it }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllTasks().collectLatest { _tasks.value = it }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllMilestones().collectLatest { _milestones.value = it }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAllFiles().collectLatest { _files.value = it }
        }
    }

    fun setSearchString(searchString: String) {
        _searchString.value = searchString
        getAllProjects.setFilter(searchString)
        getAllTasks.setFilter(searchString)
        getAllMilestones.setFilter(searchString)
        getAllFiles.setFilter(searchString)
    }

}