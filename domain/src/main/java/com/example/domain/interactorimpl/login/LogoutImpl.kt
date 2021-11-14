package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.Logout
import com.example.domain.repository.AuthRepository

class LogoutImpl(
    private val authRepository: AuthRepository
) : Logout {
    override operator fun invoke() {
        return authRepository.logOut()
    }
}