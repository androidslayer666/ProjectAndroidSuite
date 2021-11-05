package com.example.domain.interactor.login

import com.example.domain.model.User
import com.example.domain.repository.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GetSelfProfile(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        CoroutineScope(IO).launch { teamRepository.getAllPortalUsers() }
        return teamRepository.getSelfProfile()
    }
}