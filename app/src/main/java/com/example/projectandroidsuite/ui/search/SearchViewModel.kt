package com.example.projectandroidsuite.ui.search

import androidx.lifecycle.*
import com.example.domain.model.Milestone
import com.example.domain.repository.FileRepository
import com.example.domain.repository.MilestoneRepository
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.projectandroidsuite.logic.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    projectRepository: ProjectRepository,
    taskRepository: TaskRepository,
    private val milestoneRepository: MilestoneRepository,
    fileRepository: FileRepository
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

    val tasks = taskRepository.getAllTasks().asLiveData()
        .combineWith(searchString) { listTasks, searchString ->
            if (!searchString.isNullOrEmpty()) {
                listTasks?.filterTaskByFilter(TaskFilter(searchQuery = searchString))
            } else {
                listTasks
            }
        }

    val milestones = projectRepository.getAllStoredProjects().asLiveData().switchMap { projects ->
        val listMilestones = mutableListOf<Milestone>()
        viewModelScope.launch {
            projects.forEach { project ->
                milestoneRepository.getMilestonesByProjectFlow(project.id).collectLatest { milestones ->
                    listMilestones.addAll(milestones)
                }
            }
        }
        liveData {emit(listMilestones)}.combineWith(searchString) { milestones, searchString ->
            milestones?.filterMilestoneByFilter(MilestoneFilter(searchString))
        }
    }

    val files = fileRepository.getAllFiles().asLiveData().combineWith(searchString){ listFiles, searchString ->
        listFiles?.filterFileByFilter(FileFilter(searchString))
    }

    fun setSearchString(searchString: String) {
        _searchString.value = searchString
    }

}