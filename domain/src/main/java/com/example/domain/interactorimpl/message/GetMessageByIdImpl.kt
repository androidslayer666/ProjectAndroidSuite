package com.example.domain.interactorimpl.message

import com.example.domain.interactor.message.GetMessageById
import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMessageByIdImpl(
    private val messageRepository: MessageRepository
) : GetMessageById {

    override operator fun invoke(messageId: Int?): Flow<Message?> {
        return messageId?.let {
             messageRepository.getMessageById(messageId)
        } ?: flow {}
    }
}