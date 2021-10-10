package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.*
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.UserEntity
import com.example.domain.model.Milestone
import com.example.domain.model.User
import com.example.domain.repository.*
import com.example.projectandroidsuite.logic.UserFilter
import com.example.projectandroidsuite.logic.combineWith
import com.example.projectandroidsuite.logic.filterUsersByFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MilestoneCreateEditViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val projectRepository: ProjectRepository,
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private var projectId: Int? = null

    private var _milestone = MutableLiveData<Milestone>()
    val milestone: LiveData<Milestone> = _milestone

    private var _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private var _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private var _priority = MutableLiveData(false)
    val priority: LiveData<Boolean?> = _priority

    private var _responsible = MutableLiveData<User?>()
    val responsible: LiveData<User?> = _responsible

    private var userSearch = MutableLiveData<UserFilter>()

    private var _userSearchQuery = MutableLiveData<String>()
    val userSearchQuery: LiveData<String> = _userSearchQuery

    private var _endDate = MutableLiveData(Date())
    val endDate: LiveData<Date> = _endDate

    private var _milestoneCreationStatus = MutableLiveData<Result<String, String>?>()
    val subtaskCreationStatus: LiveData<Result<String, String>?> = _milestoneCreationStatus

    private var _milestoneUpdatingStatus = MutableLiveData<Result<String, String>?>()
    val subtaskUpdatingStatus: LiveData<Result<String, String>?> = _milestoneUpdatingStatus

    val userListFlow = teamRepository.getAllPortalUsers().asLiveData()
        .combineWith(userSearch) { listProject, filter ->
            if (filter != null) listProject?.filterUsersByFilter(filter)
            else listProject
        }

    init {
        viewModelScope.launch(IO) {
            teamRepository.populateAllPortalUsers()
            projectRepository.getProjects()
            //if(projectResponse is Failure<String>) _problemWithFetchingProjects.value = projectResponse.reason!!
        }
    }

    fun setMilestone(milestone: Milestone) {
        _milestone.value = milestone
        _title.value = milestone.title
        _priority.value = milestone.isKey
        _description.value = milestone.description
        _endDate.value = milestone.deadline ?: Date()
        _responsible.value = milestone.responsible
    }

    fun setTitle(string: String) {
        _title.value = string
    }

    fun setDescription(string: String) {
        _description.value = string
    }

    fun setProjectId(projectId: Int) {
        this.projectId = projectId
    }

    fun setDate(date: Date) {
        Log.d("setDate", date.toString())
        _endDate.value = date
    }

    fun setResponsible(user: User) {
        //Log.d("ProjectCreateEditodel", "setting the manager" + user.toString())
        _responsible.value = user
    }

    fun setPriority(value: Boolean) {
        Log.d("setPriority", value.toString())
        _priority.value = value
    }

    fun clearInput() {
        _title.value = ""
        _description.value = ""
        _priority.value = false
        _endDate.value = Date()
        _responsible.value = null
        _milestoneCreationStatus.value = null
        _milestoneUpdatingStatus.value = null
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        userSearch.value =
            UserFilter(query)
    }


    fun createMilestone() {
        viewModelScope.launch(IO) {
            val response = milestoneRepository.putMilestoneToProject(
                //todo shouldNot be null
                projectId ?: 0,
                Milestone(
                    id =0,
                    title = title.value ?: "",
                    responsible = responsible.value,
                    description = description.value,
                    isKey = priority.value,
                    deadline = endDate.value,
                )
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _milestoneCreationStatus.value = response
            }
        }
    }

    fun updateMilestone() {
        viewModelScope.launch(IO) {
            val response = milestoneRepository.updateMilestoneToProject(
                //todo shouldNot be null
                projectId ?: 0,
                Milestone(
                    title = title.value ?: "",
                    responsible = responsible.value,
                    description = description.value,
                    id = milestone.value?.id ?: 0,
                    deadline = endDate.value
                )
            )
            withContext(Dispatchers.Main) {
                Log.d("", response.toString())
                _milestoneCreationStatus.value = response
            }
        }
    }

}

