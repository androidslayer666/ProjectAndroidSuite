package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.CheckPortalPossibilities
import com.example.domain.repository.AuthRepository
import com.example.domain.utils.Result

class CheckPortalPossibilitiesImpl (
    private val authRepository: AuthRepository
) : CheckPortalPossibilities {
    override suspend operator fun invoke(address: String): Result<String, String> {
        return authRepository.checkPortalPossibilities(address)
    }
}