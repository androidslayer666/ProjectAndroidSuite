package com.example.domain.interactor.login

import com.example.domain.repository.AuthRepository

class RememberPortalAddress(
    private val authRepository: AuthRepository
) {

    operator fun invoke(address: String){
        authRepository.rememberPortalAddress(address)
    }
}