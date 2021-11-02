package com.example.domain.interactor.message

import com.example.domain.repository.MessageRepository
import com.example.domain.utils.Result

class DeleteMessage(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(messageId: Int, projectId: Int?): Result<String, String> {
        return messageRepository.deleteMessage(messageId, projectId)
    }
}