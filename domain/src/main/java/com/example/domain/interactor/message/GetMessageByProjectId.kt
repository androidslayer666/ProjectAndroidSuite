package com.example.domain.interactor.message

import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class GetMessageByProjectId(
    private val messageRepository: MessageRepository
) {

    operator fun invoke(projectId: Int): Flow<List<Message>> {


        return messageRepository.getMessagesByProjectId(projectId)
    }
}