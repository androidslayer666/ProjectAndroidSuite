package com.example.domain.repository

import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun populateMessageWithProjectId(projectId: Int): Result<String, Throwable>

    suspend fun updateMessageComments(messageId:Int, projectId: Int)

    fun getMessagesByProjectId(projectId: Int): Flow<List<Message>>

    suspend fun putMessageToProject(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, Throwable>

    suspend fun updateMessage(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, Throwable>

    fun getMessageById(messageId: Int): Flow<Message?>

    suspend fun deleteMessage( messageId: Int, projectId: Int?): Result<String, Throwable>

    suspend fun clearLocalCache()
}