package com.example.domain.interactor.login

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetSelfProfile {
    suspend operator fun invoke(): Flow<User?>
}