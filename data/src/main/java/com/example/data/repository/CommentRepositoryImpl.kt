package com.example.data.repository

import android.util.Log
import com.example.data.ResponseIsEmptyException
import com.example.data.dao.CommentDao
import com.example.domain.*
import com.example.domain.mappers.toCommentPost
import com.example.domain.mappers.toListCommentIds
import com.example.domain.mappers.toListEntities
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.data.endpoints.CommentEndPoint
import com.example.domain.dto.CommentDto
import com.example.domain.entities.CommentEntity
import com.example.domain.mappers.fromListCommentEntitiesToListComments
import com.example.domain.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao
) : CommentRepository {

    override suspend fun populateCommentsWithTaskId(taskId: Int): Result<String, Throwable> {
        return try {
            val comments = commentEndPoint.getTaskComment(taskId).listCommentDtos

            if (comments == null) {
                Failure(ResponseIsEmptyException())
            } else {
                deleteCommentFromDbIfDeletedOnServer(taskId, comments )
                commentDao.insertComments(comments.toListEntities(taskId))
                Success("")
            }
        } catch (e: Exception) {
            Failure(e)
        }
    }

    private suspend fun deleteCommentFromDbIfDeletedOnServer(
        taskId: Int,
        comments: List<CommentDto>
    ) {
        commentDao.getCommentsByTaskId(taskId).forEach {
            if(!comments.toListCommentIds().contains(it.id)){
                commentDao.deleteComment(it.id)
            }
        }
    }


    override fun getCommentByTaskId(taskId: Int): Flow<List<Comment>?> {
        return commentDao.getCommentsByTaskIdFlow(taskId).transform { listComments ->
            emit(listComments.fromListCommentEntitiesToListComments())
        }
    }

    override suspend fun putCommentToMessage(messageId: Int, comment: Comment): Result<String, Throwable> {
        return networkCaller(
            call = { commentEndPoint.putCommentToMessage(messageId, comment.toCommentPost()) },
            onSuccess = {populateCommentsWithTaskId(comment.taskId?:0) }
        )
    }

    override suspend fun putCommentToTask(taskId: Int, comment: Comment) : Result<String, Throwable> {
        return networkCaller(
            call = { commentEndPoint.putCommentToTask(taskId, comment.toCommentPost()) },
            onSuccess = {populateCommentsWithTaskId(taskId) }
        )
    }

    override suspend fun deleteComment(commentId: String, taskId: Int): Result<String, Throwable> {
        return networkCaller(
            call = { commentEndPoint.deleteComment(commentId) },
            onSuccess = {  populateCommentsWithTaskId(taskId) }
        )
    }

    override suspend fun clearLocalCache() {
        commentDao.clearLocalCache()
    }
}