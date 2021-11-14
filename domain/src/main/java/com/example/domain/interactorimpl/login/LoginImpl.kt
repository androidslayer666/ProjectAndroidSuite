package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.Login
import com.example.domain.model.LoginResponse
import com.example.domain.repository.AuthRepository
import com.example.domain.utils.Result

class LoginImpl(
    private val authRepository: AuthRepository
) : Login {
    override suspend operator fun invoke(
        address: String,
        email: String,
        password: String,
        code: Int?
    ): Result<LoginResponse, String> {

        return authRepository.login(
            address, email, password, code
        )
    }
}