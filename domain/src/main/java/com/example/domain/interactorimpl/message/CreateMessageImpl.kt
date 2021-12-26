package com.example.domain.interactorimpl.message

import com.example.domain.interactor.message.CreateMessage
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.repository.MessageRepository
import com.example.domain.utils.Result
import com.example.domain.utils.toStringString

class CreateMessageImpl(
    private val messageRepository:MessageRepository
) : CreateMessage {

    override suspend operator fun invoke(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String> {
        return messageRepository.putMessageToProject(projectId, message, participants).toStringString()
    }

}