package com.example.domain.interactor.user

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetAllUsers {
    suspend operator fun invoke(): Flow<MutableList<User>>
    fun setChosenUsersList(list: List<User>?)
    fun setFilter(query: String)

}