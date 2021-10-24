package com.example.domain.repositoryimpl

import android.util.Log
import com.example.database.dao.CommentDao
import com.example.database.dao.MessageDao
import com.example.domain.*
import com.example.domain.mappers.*
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.domain.repository.*
import com.example.network.endpoints.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.lang.Exception
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
                    val comments = commentEndPoint.getMessageComments(message.id).listCommentDtos

                    val messageFromDb = messageDao.getMessageByMessageId(message.id)

                    Log.d(
                        "populateMessageWicketId",
                        "messages from db" + messageFromDb.listMessages?.map { it.id }.toString()
                    )
                    messageFromDb.listMessages?.forEach {
                        if (comments?.toListCommentIds()?.contains(it.id) != true
                        ) {
                            Log.d("populateMessageWiectId", "deleting comment" + it.id)
                            commentDao.deleteComment(it.id)
                        }
                    }

                    val messageEntities = message.toEntity(projectId)
                    if (comments != null) {
                        Log.d(
                            "populateMessageWiectId",
                            "inserting comments" + comments.map { it.id }.toString()
                        )
                        messageEntities.listMessages = arrangingComments(comments.toListEntities())
                    }
                    messageDao.insertMessages(listOf(messageEntities))
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


    override suspend fun deleteMessage(messageId: Int): Result<String, String> {

        return networkCaller(
            call = { messageEndPoint.deleteMessage(messageId) },
            onSuccess = { },
            onSuccessString = "Message deleted successfully",
            onFailureString = "Having problem while deleting the message, please check the network connection"
        )
    }
}