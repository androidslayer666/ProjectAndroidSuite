package com.example.domain.interactor.message

import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface GetMessageById {
    operator fun invoke(messageId: Int?): Flow<Message?>
}