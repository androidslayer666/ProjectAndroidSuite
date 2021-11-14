package com.example.domain.interactorimpl.login

import com.example.domain.interactor.login.RememberPortalAddress
import com.example.domain.repository.AuthRepository

class RememberPortalAddressImpl(
    private val authRepository: AuthRepository
) : RememberPortalAddress {

    override operator fun invoke(address: String){
        authRepository.rememberPortalAddress(address)
    }
}