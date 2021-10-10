package com.example.projectandroidsuite.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.database.entities.MilestoneEntity
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

        //todo implement population on the repository side

    val milestones = projectRepository.getAllStoredProjects().asLiveData().switchMap { projects ->
        val listMilestones = mutableListOf<Milestone>()
        viewModelScope.launch {
            projects.forEach { project ->
                milestoneRepository.getMilestonesByProjectFlow(project.id).collectLatest { milestones ->
                    listMilestones.addAll(milestones)
                }
            }
        }
        liveData {emit(listMilestones)}.combineWith(searchString) { listMilestones, searchString ->
            listMilestones?.filterMilestoneByFilter(MilestoneFilter(searchString))
        }
    }

    val files = fileRepository.getAllFiles().asLiveData().combineWith(searchString){ listFiles, searchString ->
        listFiles?.filterFileByFilter(FileFilter(searchString))
    }


    fun clearSearchString(){
        _searchString.value = ""
    }

    fun setSearchString(searchString: String) {
        _searchString.value = searchString
    }

}