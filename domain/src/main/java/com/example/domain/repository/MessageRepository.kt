package com.example.domain.repository

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Message
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun populateMessageWithProjectId(projectId: Int): Result<String, String>

    suspend fun updateMessageComments(messageId:Int, projectId: Int)

    fun getMessagesByProjectId(projectId: Int): Flow<List<Message>>

    suspend fun putMessageToProject(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String>

    suspend fun updateMessage(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String>


    suspend fun deleteMessage( messageId: Int): Result<String, String>
}