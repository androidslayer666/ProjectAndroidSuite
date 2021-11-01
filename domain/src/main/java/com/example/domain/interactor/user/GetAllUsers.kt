package com.example.domain.interactor.user

import android.util.Log
import com.example.domain.utils.UserFilter
import com.example.domain.utils.filterUsersByFilter
import com.example.domain.model.User
import com.example.domain.repository.TeamRepository
import com.example.domain.utils.getListUserIdsFromList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GetAllUsers(
    private val teamRepository: TeamRepository
) {

    private var userFilter = MutableStateFlow<UserFilter?>(UserFilter(""))

    private var listChosenUsers = MutableStateFlow<String?>("")

    suspend operator fun invoke(): Flow<MutableList<User>> {
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

    fun setFilter(query: String) {
        userFilter.value = UserFilter(query)
    }

    fun setChosenUsersList(list: List<User>?) {
        listChosenUsers.value = list?.map{it.id}.toString().drop(1).dropLast(1)
    }

}