package com.example.domain.interactor.login

import com.example.domain.repository.AuthRepository
import com.example.domain.utils.Result

class CheckPortalPossibilities (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(address: String): Result<String, String> {
        return authRepository.checkPortalPossibilities(address)
    }
}