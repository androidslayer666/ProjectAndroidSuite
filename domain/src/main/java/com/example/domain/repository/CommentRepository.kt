package com.example.domain.repository

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun populateCommentsWithTaskId(taskId: Int): Result<String, Throwable>

    fun getCommentByTaskId(taskId: Int): Flow<List<Comment>?>

    suspend fun putCommentToMessage(messageId: Int, comment: Comment): Result<String, Throwable>

    suspend fun putCommentToTask(taskId: Int, comment: Comment) : Result<String, Throwable>

    suspend fun deleteComment(commentId: String, taskId: Int): Result<String, Throwable>

}