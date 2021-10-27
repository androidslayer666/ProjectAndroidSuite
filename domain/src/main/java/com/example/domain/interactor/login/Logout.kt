package com.example.domain.interactor.login

import com.example.domain.repository.AuthRepository

class Logout(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        return authRepository.logOut()
    }
}