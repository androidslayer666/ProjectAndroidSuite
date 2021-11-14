package com.example.domain.interactorimpl.user

import com.example.domain.filters.user.UserFilter
import com.example.domain.filters.user.filterUsersByFilter
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.model.User
import com.example.domain.repository.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetAllUsersImpl(
    private val teamRepository: TeamRepository
) : GetAllUsers  {
    private var userFilter = MutableStateFlow<UserFilter?>(UserFilter(""))

    private var listChosenUsers = MutableStateFlow<String?>("")

    override suspend operator fun invoke(): Flow<MutableList<User>> {
        CoroutineScope(Dispatchers.IO).launch {
            teamRepository.populateAllPortalUsers()
        }

        return teamRepository.getAllPortalUsers()
            .combine(listChosenUsers){ users, chosenUsers ->
                users.forEach { user ->
                    user.chosen = chosenUsers?.contains(user.id)  == true
                }
                users
            }
            .combine(userFilter) { users, filter ->
            users.filterUsersByFilter(filter)
        }
    }

    override fun setFilter(query: String) {
        userFilter.value = UserFilter(query)
    }

    override fun setChosenUsersList(list: List<User>?) {
        listChosenUsers.value = list?.map{it.id}.toString().drop(1).dropLast(1)
    }

}