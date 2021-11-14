package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.CheckIfAuthenticated
import com.example.domain.repository.AuthRepository

class CheckIfAuthenticatedImpl (
    private val authRepository: AuthRepository
        ) : CheckIfAuthenticated {

    override operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}