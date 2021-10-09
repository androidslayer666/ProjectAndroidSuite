package com.example.domain.repository

import com.example.database.entities.CommentEntity
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun populateCommentsWithTaskId(taskId: Int): Result<String, String>

    fun getCommentByTaskId(taskId: Int?): Flow<List<CommentEntity>?>

    suspend fun putCommentToMessage(messageId: Int, comment: CommentEntity): Result<String, String>

    suspend fun putCommentToTask(taskId: Int, comment: CommentEntity) : Result<String, String>

    suspend fun deleteComment(commentId: String, taskId: Int? = null): Result<String, String>

}