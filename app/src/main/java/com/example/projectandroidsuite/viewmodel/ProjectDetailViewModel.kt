package com.example.projectandroidsuite.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.*
import com.example.domain.SessionManager
import com.example.domain.TestRepository
import com.example.domain.repository.*
import com.example.network.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val teamRepository: TeamRepository,
    private val milestoneRepository: MilestoneRepository,
    private val messageRepository: MessageRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private var _currentProject = MutableLiveData<ProjectEntity>()
    val currentProject: LiveData<ProjectEntity> = _currentProject

    private var _team = MutableLiveData<List<UserEntity>>()
    val team: LiveData<List<UserEntity>> = _team

    private var _listTaskAndMilestones = MutableLiveData<List<UniversalEntity>>()
    val listTaskAndMilestones: LiveData<List<UniversalEntity>> = _listTaskAndMilestones

    private var _listDiscussions = MutableLiveData<List<MessageEntity>>()
    val listDiscussions: LiveData<List<MessageEntity>> = _listDiscussions

    var _listComments = MutableLiveData<List<Pair<MessageEntity, List<CommentEntity>>>>()
    val listComments: LiveData<List<Pair<MessageEntity, List<CommentEntity>>>> = _listComments





    fun deleteProject() {
        if (currentProject.value != null) {
            CoroutineScope(IO).launch {
                projectRepository.deleteProject(currentProject.value!!)
            }
        }
    }

    fun getTaskAndMilestones(projectId: Int?): Flow<List<UniversalEntity>> {
        //Log.d("getTaskAndMilestones", projectId.toString())
        if (projectId != null) {
            val tasks = taskRepository.getTasksByProject(projectId)
            val milestones = milestoneRepository.getMilestoneByProject(projectId)
            return tasks.combine(milestones) { tasks, milestones ->
                //Log.d("getTaskAndMilestones", milestones.toString())
                val universalList: MutableList<UniversalEntity> = mutableListOf()
                val newTaskList = tasks.toMutableList()
                for (milestone in milestones) {
                    universalList.add(milestone)
                    for (task in tasks) {
                        if (task.milestoneId == milestone.id) {
                            universalList.add(task)
                            newTaskList.remove(task)
                        }
                    }
                }
                universalList.addAll(newTaskList)
                _listTaskAndMilestones.value = universalList
                return@combine universalList
            }
        }
        return flow { }
    }

    fun getCurrentProject(projectId: Int) {
        viewModelScope.launch(IO) {

            //Log.d("getTaskAndMilestones", projectId.toString())
            val project = projectRepository.getProjectFromDbById(projectId)
            val team = project.team

            withContext(Dispatchers.Main) {
                //Log.d("getCurrentProject", "setting of the current project")
                _currentProject.value = project
                _team.value = team!!
            }

            async {
                //Log.d("getCurrentProject", "done setting of the current project")
                messageRepository.getCommentByProjectId(projectId).collectLatest {
                    //Log.d("getCurrentProject", "getting something from message repository")
                    withContext(Dispatchers.Main) {
                        //Log.d("getCurrentProject", it.toString())
                        _listDiscussions.value = it
                        for (message in it) {
                            commentRepository.populateCommentsWithMessageId(message.id)
                        }
                    }
                }
            }

            async {
                //Log.d("getCurrentProject", "populating messages")
                messageRepository.populateMessageWithProjectId(projectId)
                //Log.d("getCurrentProject", "populating tasks")
                taskRepository.populateTasksByProject(projectId)
                //Log.d("getCurrentProject", "populating milestones")
                milestoneRepository.populateMilestonesByProject(projectId)
            }
        }
    }

    //fun getListTasksAndMilestones(projectId: Int):  Flow<List<MessageEntity>>
}