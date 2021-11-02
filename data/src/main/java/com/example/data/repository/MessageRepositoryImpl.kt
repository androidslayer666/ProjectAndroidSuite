package com.example.data.repository

import android.util.Log
import com.example.data.dao.CommentDao
import com.example.data.dao.MessageDao
import com.example.data.endpoints.CommentEndPoint
import com.example.data.endpoints.MessageEndPoint
import com.example.domain.entities.MessageEntity
import com.example.domain.mappers.*
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.repository.MessageRepository
import com.example.domain.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val messageEndPoint: MessageEndPoint,
    private val messageDao: MessageDao,
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao
) : MessageRepository {

    override suspend fun populateMessageWithProjectId(projectId: Int): Result<String, String> {
        try {
            //Log.d("CommentRepository", projectId.toString())
            val messages = messageEndPoint.getMessagesWithProject(projectId).listMessages

            val messagesFromDb = messageDao.getMessageByProjectId(projectId)
            messagesFromDb.forEach {
                if (messages?.toListMessageIds()?.contains(it.id) != true) {
                    messageDao.deleteMessage(it.id)
                }
            }

            return if (messages != null) {
                for (message in messages) {
                    putCommentsToMessage(message.toEntity(projectId))
                }
                Success("Messages are populated")
            } else {
                Failure("Can't download messages")
            }
        } catch (e: Exception) {
            Log.d("MessageRepository", "caught an exception : ${e.message}")
            return Failure("Can't download messages")
        }
    }

    override suspend fun updateMessageComments(messageId: Int, projectId: Int) {
        val message = messageEndPoint.getMessageWithId(messageId).message
        message?.toEntity(projectId)?.let { putCommentsToMessage(it) }
    }

    private suspend fun putCommentsToMessage(message: MessageEntity) {
        val comments = commentEndPoint.getMessageComments(message.id).listCommentDtos

        if (comments != null) {
            Log.d(
                "populateMessageWiectId",
                "inserting comments" + comments.map { it.id }.toString()
            )
            message.listComments = arrangingComments(comments.toListEntities())
        }

        val commentFromDb = commentDao.getCommentsByMessageId(message.id)
        commentFromDb.forEach {
            if (comments?.toListCommentIds()?.contains(it.id) != true) {
                commentDao.deleteComment(it.id)
            }
        }
        messageDao.insertMessage(message)
    }


    override fun getMessagesByProjectId(projectId: Int): Flow<List<Message>> {
        return messageDao.getMessageByProjectIdFlow(projectId)
            .transform { emit(it.fromListMessageEntitiesToListMessages()) }
    }


    override suspend fun putMessageToProject(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String> {
        Log.d("ProjectRepository", "Started creating message  $message")

        return networkCaller(
            call = {
                messageEndPoint.putMessageToProject(
                    projectId,
                    message.toMessagePost(participants = participants)
                )
            },
            onSuccess = { populateMessageWithProjectId(projectId) },
            onSuccessString = "Message added successfully",
            onFailureString = "Having problem while creating the message, please check the network connection"
        )
    }

    override suspend fun updateMessage(
        projectId: Int,
        message: Message,
        participants: List<User>
    ): Result<String, String> {
        Log.d("ProjectRepository", "Started creating message  $message")

        return networkCaller(
            call = {
                messageEndPoint.updateMessage(
                    message.id,
                    message.toMessagePost(projectId, participants)
                )
            },
            onSuccess = { populateMessageWithProjectId(projectId) },
            onSuccessString = "Message added successfully",
            onFailureString = "Having problem while creating the message, please check the network connection"
        )
    }


    override suspend fun deleteMessage(messageId: Int, projectId: Int?): Result<String, String> {

        return networkCaller(
            call = { messageEndPoint.deleteMessage(messageId) },
            onSuccess = { projectId?.let { populateMessageWithProjectId(it) } },
            onSuccessString = "Message deleted successfully",
            onFailureString = "Having problem while deleting the message, please check the network connection"
        )
    }
}