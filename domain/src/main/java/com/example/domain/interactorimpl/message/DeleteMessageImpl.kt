package com.example.domain.interactorimpl.message

import com.example.domain.interactor.message.DeleteMessage
import com.example.domain.repository.MessageRepository
import com.example.domain.utils.Result

class DeleteMessageImpl(
    private val messageRepository: MessageRepository
) : DeleteMessage {
    override suspend operator fun invoke(messageId: Int, projectId: Int?): Result<String, String> {
        return messageRepository.deleteMessage(messageId, projectId)
    }
}