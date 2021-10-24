package com.example.domain.repository

import com.example.domain.Result
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun populateAllPortalUsers() : Result<String, String>

    fun getAllPortalUsers() : Flow<List<User>>

    suspend fun getSelfProfile(): User?

}