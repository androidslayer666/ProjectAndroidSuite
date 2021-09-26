package com.example.domain.repository

import android.util.Log
import com.example.database.dao.CommentDao
import com.example.database.dao.MessageDao
import com.example.database.entities.MessageEntity
import com.example.database.entities.UserEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.domain.mappers.toMessagePost
import com.example.domain.mappers.toMilestonePost
import com.example.network.endpoints.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageEndPoint: MessageEndPoint,
    private val messageDao: MessageDao,
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao
) {

    suspend fun populateMessageWithProjectId(projectId: Int): Result<String, String> {
        try {
            //Log.d("CommentRepository", projectId.toString())
            var messages = messageEndPoint.getMessagesWithProject(projectId).listMessages
            //Log.d("CommentRepository", messages.toString())

            return if (messages != null) {
                for (message in messages) {
                    val comments = commentEndPoint.getMessageComments(message.id).listCommentDtos
                    //Log.d("populateMessageWiectId", comments.toString())
                    val message = message.toEntity(projectId)
                    if (comments != null) {
                        message.listMessages = arrangingComments(comments.toListEntities())
                    }
                    messageDao.insertMessages(listOf(message))
                }
                Success("Comments are populated")
            }else {
                Failure("Can't download comments")
            }
        } catch (e: Exception) {
            Log.d("MessageRepository", "caught an exception : ${e.message}")
            return Failure("Can't download comments")
        }
    }



    fun getMessagesByProjectId(projectId: Int): Flow<List<MessageEntity>> {
        return messageDao.getMessageByProjectId(projectId)
    }


    suspend fun putMessageToProject(projectId : Int, message: MessageEntity, participants: List<UserEntity>): Result<String, String> {
        Log.d("ProjectRepository", "Started creating milestone  $message")
        try {
            val response = messageEndPoint.putMessageToProject(projectId, message.toMessagePost(participants))

            Log.d("ProjectRepository", response.toString())
            if (response != null) {
                Log.d("ProjectRepository", "Project created")
                //because response from server doesn't give id
                populateMessageWithProjectId(projectId)
                return Success("Project successfully created")
            } else {
                return Failure("Project was not created due to a server problem")
            }
        } catch (e: Exception) {
            Log.d(
                "ProjectRepository",
                "tried to create a project but caught an exception: ${e.message}"
            )
            return Failure("Project was not created, please check network or ask the developer to fix this")
        }
    }
}