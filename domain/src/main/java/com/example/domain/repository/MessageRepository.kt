package com.example.domain.repository

import android.util.Log
import com.example.database.dao.CommentDao
import com.example.database.dao.MessageDao
import com.example.database.entities.MessageEntity
import com.example.database.entities.UserEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMessagePost
import com.example.network.dto.MessageDto
import com.example.network.endpoints.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageEndPoint: MessageEndPoint,
    private val messageDao: MessageDao,
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao,
    private val commentRepository: CommentRepository
) {

    suspend fun populateMessageWithProjectId(projectId: Int): Result<String, String> {
        try {
            //Log.d("CommentRepository", projectId.toString())
            var messages = messageEndPoint.getMessagesWithProject(projectId).listMessages

            val messagesFromDb = messageDao.getMessageByProjectId(projectId)
            messagesFromDb.forEach {
                if(messages?.toListMessageIds()?.contains(it.id) != true) {
                    messageDao.deleteMessage(it.id)
                }
            }

            return if (messages != null) {
                for (message in messages) {
                    val comments = commentEndPoint.getMessageComments(message.id).listCommentDtos
                    Log.d("populateMessageWiectId", "messages from net" + comments?.toString())

                    val messageFromDb = messageDao.getMessageByMessageId(message.id)
                    if (messageFromDb != null) {

                        Log.d("populateMessageWiectId", "messages from db" + messageFromDb.listMessages?.map { it.id }.toString())
                        messageFromDb.listMessages?.forEach {
                            if (comments?.toListCommentIds()?.contains(it.id) != true
                            ) {
                                Log.d("populateMessageWiectId", "deleting comment" + it.id)
                                commentDao.deleteComment(it.id)
                            }
                        }
                    }

                    val message = message.toEntity(projectId)
                    if (comments != null) {
                        Log.d("populateMessageWiectId", "inserting comments" + comments?.map { it.id }.toString())
                        message.listMessages = arrangingComments(comments.toListEntities())
                    }
                    messageDao.insertMessages(listOf(message))
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


    fun getMessagesByProjectId(projectId: Int): Flow<List<MessageEntity>> {
        return messageDao.getMessageByProjectIdFlow(projectId)
    }


    suspend fun putMessageToProject(
        projectId: Int,
        message: MessageEntity,
        participants: List<UserEntity>
    ): Result<String, String> {
        Log.d("ProjectRepository", "Started creating message  $message")

        return networkCaller(
            call = { messageEndPoint.putMessageToProject(projectId, message.toMessagePost(participants = participants)) },
            onSuccess = { populateMessageWithProjectId(projectId) },
            onSuccessString = "Message added successfully",
            onFailureString = "Having problem while creating the message, please check the network connection"
        )
    }

    suspend fun updateMessage(
        projectId: Int,
        message: MessageEntity,
        participants: List<UserEntity>
    ): Result<String, String> {
        Log.d("ProjectRepository", "Started creating message  $message")

        return networkCaller(
            call = { messageEndPoint.updateMessage(message.id, message.toMessagePost(projectId, participants)) },
            onSuccess = { populateMessageWithProjectId(projectId) },
            onSuccessString = "Message added successfully",
            onFailureString = "Having problem while creating the message, please check the network connection"
        )
    }


    suspend fun deleteMessage( messageId: Int): Result<String, String>{

        return networkCaller(
            call = { messageEndPoint.deleteMessage(messageId) },
            onSuccess = {  },
            onSuccessString = "Message deleted successfully",
            onFailureString = "Having problem while deleting the message, please check the network connection"
        )
    }
}