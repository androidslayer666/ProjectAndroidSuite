package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.GetSelfProfile
import com.example.domain.model.User
import com.example.domain.repository.TeamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetSelfProfileImpl(
    private val teamRepository: TeamRepository
) : GetSelfProfile {
    override suspend operator fun invoke(): Flow<User?> {
        CoroutineScope(IO).launch { teamRepository.getAllPortalUsers() }
        return teamRepository.getSelfProfile()
    }
}