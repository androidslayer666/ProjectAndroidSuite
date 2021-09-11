package com.example.domain.repository

import android.util.Log
import com.example.database.dao.CommentDao
import com.example.database.dao.MessageDao
import com.example.database.entities.CommentEntity
import com.example.database.entities.MessageEntity
import com.example.domain.mappers.toEntity
import com.example.domain.mappers.toListEntities
import com.example.network.dto.CommentDto
import com.example.network.endpoints.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageEndPoint: MessageEndPoint,
    private val messageDao: MessageDao,
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao

) {

    suspend fun populateMessageWithProjectId(projectId: Int) {
        //Log.d("CommentRepository", projectId.toString())
        var messages = messageEndPoint.getMessagesWithProject(projectId).listComments
        //Log.d("CommentRepository", messages.toString())

        if (messages != null) {
            for(message in messages){
                val comments = commentEndPoint.getMessageComments(message.id)
                //Log.d("populateMessageWiectId", comments.listCommentDtos?.size.toString())
                val message = message.toEntity(projectId)
                if(comments.listCommentDtos !=null){
                    message.listMessages = arrangingComments(comments.listCommentDtos!!).toListEntities()
                }
                messageDao.insertMessages(listOf(message))
            }
        }
    }


    fun getCommentByProjectId(projectId: Int): Flow<List<MessageEntity>> {
        return messageDao.getMessageByProjectId(projectId)
    }


}