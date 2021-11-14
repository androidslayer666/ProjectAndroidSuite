package com.example.domain.interactor.message

import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.utils.Result

interface UpdateMessage {
    suspend operator fun invoke(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String>
}