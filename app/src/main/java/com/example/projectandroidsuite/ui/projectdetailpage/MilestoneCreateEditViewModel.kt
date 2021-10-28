package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.milestone.PutMilestoneToProject
import com.example.domain.interactor.milestone.UpdateMilestone
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.Milestone
import com.example.domain.model.User
import com.example.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MilestoneCreateEditViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    private val putMilestoneToProject: PutMilestoneToProject,

    private val updateMilestone: UpdateMilestone
) : ViewModel() {

    private var projectId: Int? = null

    private var _milestone = MutableStateFlow<Milestone?>(null)
    val milestone: StateFlow<Milestone?> = _milestone

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private var _priority = MutableStateFlow(false)
    val priority: StateFlow<Boolean?> = _priority

    private var _responsible = MutableStateFlow<User?>(null)
    val responsible: StateFlow<User?> = _responsible

    private var _userSearchQuery = MutableStateFlow("")
    val userSearchQuery: StateFlow<String> = _userSearchQuery

    private var _endDate = MutableStateFlow(Date())
    val endDate: StateFlow<Date> = _endDate

    private var _milestoneCreationStatus = MutableStateFlow<Result<String, String>?>(null)
    val subtaskCreationStatus: StateFlow<Result<String, String>?> = _milestoneCreationStatus

    private var _milestoneUpdatingStatus = MutableStateFlow<Result<String, String>?>(null)
    val subtaskUpdatingStatus: StateFlow<Result<String, String>?> = _milestoneUpdatingStatus

    private var _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users

    init {
        viewModelScope.launch(IO) {
            getAllUsers().collectLatest { _users.value = it }
        }
    }

    fun setUserSearch(query: String) {
        _userSearchQuery.value = query
        getAllUsers.setFilter(query)
    }

    fun setMilestone(milestone: Milestone) {
        _milestone.value = milestone
        _title.value = milestone.title ?: ""
        _priority.value = milestone.isKey ?: false
        _description.value = milestone.description ?: ""
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

    fun createMilestone() {
        viewModelScope.launch(IO) {
            val response = putMilestoneToProject(
                projectId ?: 0,
                Milestone(
                    id = 0,
                    title = title.value,
                    responsible = responsible.value,
                    description = description.value,
                    isKey = priority.value,
                    deadline = endDate.value,
                )
            )
            _milestoneCreationStatus.value = response
        }
    }

    fun updateMilestone() {
        viewModelScope.launch(IO) {
            val response = updateMilestone(
                projectId ?: 0,
                Milestone(
                    title = title.value,
                    responsible = responsible.value,
                    description = description.value,
                    id = milestone.value?.id ?: 0,
                    deadline = endDate.value
                )
            )
            _milestoneCreationStatus.value = response
        }
    }

}

