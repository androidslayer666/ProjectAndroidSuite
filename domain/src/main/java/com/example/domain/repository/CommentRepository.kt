package com.example.domain.repository

import android.util.Log
import com.example.database.dao.CommentDao
import com.example.database.entities.CommentEntity
import com.example.domain.mappers.toListEntities
import com.example.network.dto.CommentDto
import com.example.network.endpoints.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao
) {

    suspend fun populateCommentsWithTaskId(taskId: Int) {
        //Log.d("CommentRepository", taskId.toString())
        var comments = commentEndPoint.getTaskComment(taskId).listCommentDtos
        //Log.d("CommentRepository", comments.toString())

        if (comments != null) {
            comments = arrangingComments(comments)
        }
        comments?.toListEntities(taskId = taskId)?.let { commentDao.insertComments(it) }
    }

    suspend fun populateCommentsWithMessageId(messageId: Int) {
        Log.d("CommentRepository", messageId.toString())
        var comments = commentEndPoint.getMessageComments(messageId).listCommentDtos

        if (comments != null) {
            comments = arrangingComments(comments)
        }
        comments?.toListEntities(messageId = messageId)?.let {
            Log.d("CommentRepository", it.toString())
            commentDao.insertComments(it)
        }
    }


    fun getCommentByTaskId(taskId: Int): Flow<List<CommentEntity>> {
        return commentDao.getCommentsByTaskId(taskId)
    }

    suspend fun getCommentsByMessageId(messageId: Int): List<CommentEntity> {
        return commentDao.getCommentsByMessageId(messageId)
    }

}