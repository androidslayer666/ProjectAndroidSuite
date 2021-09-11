package com.example.projectandroidsuite.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.ProjectRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.repository.TeamRepository

import com.example.projectandroidsuite.ui.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ProjectCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {


    private var _project = MutableLiveData<ProjectEntity>()
    val project: LiveData<ProjectEntity> = _project

    private var _userList = MutableLiveData<MutableList<UserEntity>>()
    val userList: LiveData<MutableList<UserEntity>> = _userList

    private var chosenProjectId by Delegates.notNull<Int>()

    init {
        viewModelScope.launch(IO) {
            teamRepository.populateAllPortalUsers()
            projectRepository.getProjects()


            withContext(Dispatchers.Main) {
                teamRepository.getAllPortalUsers().collectLatest {
                    _userList.value = it.toMutableList()
                }
            }
        }
    }

    fun setProject(project: ProjectEntity) {
        //Log.d("setTask", project.toString())
        _project.value = project
    }

    fun clearChosenUsers() {
        if (userList.value != null) {
            for (task in userList.value!!) {
                task.chosen = null
            }
        }
    }

    fun addOrRemoveUser(user: UserEntity) {
        Log.d("addOrRemoveUser", user.toString())
        if (project.value?.team != null) {
            val listIds = project.value?.team?.getListIds()!!
            if (listIds.contains(user.id)) {
                project.value?.team?.remove(project.value?.team?.getUserById(user.id))
                Log.d("addOrRemoveUser", project.value.toString())
                _project.forceRefresh()
            } else {
                project.value?.team?.add(user)
                Log.d("addOrRemoveUser", project.value.toString())
                _project.forceRefresh()
            }
        }else {
            project.value?.team = mutableListOf(user)
        }
    }

    fun setTitle(string: String) {
        _project.value?.title = string
    }

    fun setDescription(string: String) {
        _project.value?.description = string
    }

    fun setResponsible(user : UserEntity){
        Log.d("ProjectCreateEditodel", "setting the manager" + user.toString())
        _project.value?.responsible = user
    }

    fun createProject() {
        if (project.value != null) {
            CoroutineScope(IO).launch {
                projectRepository.createProject(project.value!!)
            }
        }
    }

    //todo Update
    fun updateProject(project: ProjectEntity?) {
        if (project != null) {
            CoroutineScope(IO).launch {
                projectRepository.updateProject(project)
            }
        }
    }


    fun createProject(project: ProjectEntity) {
        CoroutineScope(IO).launch {
            projectRepository.createProject(project)
        }
    }


}

