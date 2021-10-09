package com.example.domain.repository

import android.util.Log
import com.example.database.entities.MessageEntity
import com.example.database.entities.UserEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMessagePost
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface MessageRepository {

    suspend fun populateMessageWithProjectId(projectId: Int): Result<String, String>


    fun getMessagesByProjectId(projectId: Int): Flow<List<MessageEntity>>

    suspend fun putMessageToProject(
        projectId: Int,
        message: MessageEntity,
        participants: List<UserEntity>
    ): Result<String, String>

    suspend fun updateMessage(
        projectId: Int,
        message: MessageEntity,
        participants: List<UserEntity>
    ): Result<String, String>


    suspend fun deleteMessage( messageId: Int): Result<String, String>
}