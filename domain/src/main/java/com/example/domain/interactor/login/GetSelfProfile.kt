package com.example.domain.interactor.login

import com.example.domain.model.User
import com.example.domain.repository.TeamRepository

class GetSelfProfile(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(): User? {
        return teamRepository.getSelfProfile()
    }
}