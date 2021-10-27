package com.example.domain.interactor.user

import android.util.Log
import com.example.domain.ProjectFilter
import com.example.domain.ProjectStatus
import com.example.domain.UserFilter
import com.example.domain.filterUsersByFilter
import com.example.domain.model.Project
import com.example.domain.model.User
import com.example.domain.repository.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetAllUsers(
    private val teamRepository: TeamRepository
) {

    private var userFilter = MutableStateFlow<UserFilter?>(null)

    suspend operator fun invoke(): Flow<MutableList<User>> {
        CoroutineScope(Dispatchers.IO).launch {
            teamRepository.populateAllPortalUsers()
        }

        return teamRepository.getAllPortalUsers().combine(userFilter) { users, filter ->
            users.filterUsersByFilter(filter)
        }
    }

    fun setFilter(query: String) {
        userFilter.value = UserFilter(query)
    }

}