package com.example.domain.interactorimpl.message

import com.example.domain.interactor.message.GetMessageByProjectId
import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GetMessageByProjectIdImpl(
    private val messageRepository: MessageRepository
) : GetMessageByProjectId {

    override operator fun invoke(projectId: Int?): Flow<List<Message>> {
        return projectId?.let {
            CoroutineScope(Dispatchers.IO).launch {
                messageRepository.populateMessageWithProjectId(it)
            }
             messageRepository.getMessagesByProjectId(projectId ?: 0)
        } ?: flow {}
    }
}