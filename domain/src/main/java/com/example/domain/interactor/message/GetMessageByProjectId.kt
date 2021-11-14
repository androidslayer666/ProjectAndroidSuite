package com.example.domain.interactor.message

import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface GetMessageByProjectId {
    operator fun invoke(projectId: Int?): Flow<List<Message>>
}