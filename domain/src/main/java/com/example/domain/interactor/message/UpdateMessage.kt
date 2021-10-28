package com.example.domain.interactor.message

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.repository.MessageRepository

class UpdateMessage(
    private val messageRepository:MessageRepository
) {

    suspend operator fun invoke(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String> {
        return messageRepository.updateMessage(projectId, message, participants)
    }

}