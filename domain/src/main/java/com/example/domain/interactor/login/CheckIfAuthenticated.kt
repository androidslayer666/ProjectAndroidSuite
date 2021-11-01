package com.example.domain.interactor.login

import com.example.domain.repository.AuthRepository

class CheckIfAuthenticated (
    private val authRepository: AuthRepository
        ) {

    operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}