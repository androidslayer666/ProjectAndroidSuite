package com.example.domain.repository

import android.util.Log
import com.example.database.entities.UserEntity
import com.example.domain.mappers.toListUserEntity
import com.example.domain.mappers.toUserEntity
import com.example.domain.model.User
import com.example.network.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun populateAllPortalUsers() : Result<String, String>

    fun getAllPortalUsers() : Flow<List<User>>

    suspend fun getSelfProfile(): User?

}