package com.example.domain.interactor.message

import com.example.domain.Result
import com.example.domain.repository.MessageRepository

class DeleteMessage(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(messageId: Int): Result<String, String> {
        return messageRepository.deleteMessage(messageId)
    }
}