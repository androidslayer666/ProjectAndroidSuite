package com.example.domain.interactorimpl.message

import com.example.domain.interactor.message.UpdateMessage
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.repository.MessageRepository
import com.example.domain.utils.Result
import com.example.domain.utils.toStringString

class UpdateMessageImpl(
    private val messageRepository:MessageRepository
) : UpdateMessage {
    override suspend operator fun invoke(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String> {
        return messageRepository.updateMessage(projectId, message, participants).toStringString()
    }

}