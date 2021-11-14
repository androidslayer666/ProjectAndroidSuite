package com.example.domain.interactor.login

import com.example.domain.utils.Result

interface CheckPortalPossibilities {
    suspend operator fun invoke(address: String): Result<String, String>
}