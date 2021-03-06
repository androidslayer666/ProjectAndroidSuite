package com.example.domain.repository

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun populateAllPortalUsers() : Result<String, Throwable>

    fun getAllPortalUsers() : Flow<List<User>>

    suspend fun getSelfProfile(): Flow<User?>

    suspend fun clearLocalCache()

}