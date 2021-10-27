package com.example.data.repository

import android.util.Log
import com.example.data.dao.CommentDao
import com.example.domain.*
import com.example.domain.mappers.fromListCommentEntitiesToListComments
import com.example.domain.mappers.toCommentPost
import com.example.domain.mappers.toListCommentIds
import com.example.domain.mappers.toListEntities
import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.data.endpoints.CommentEndPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao
) : CommentRepository {

    override suspend fun populateCommentsWithTaskId(taskId: Int): Result<String, String> {
        try {
            Log.d("CommentRepository", taskId.toString())
            val comments = commentEndPoint.getTaskComment(taskId).listCommentDtos
            Log.d("CommentRepository", comments.toString())

            return if (comments == null) {
                Failure("Can't download comments")
            } else {
                arrangingComments(comments.toListEntities(taskId)).let {
                    commentDao.insertComments(it)
                }
                commentDao.getCommentsByTaskId(taskId).forEach {
                    //Log.d("CommentRepository", "id " + it.id)
                    if(!comments.toListCommentIds().contains(it.id)){
                        //Log.d("CommentRepository", "DeleteMessage with id " + it.id)
                        commentDao.deleteComment(it.id)
                    }
                }
                Success("Comments are populated")
            }
        } catch (e: Exception) {
            Log.e("CommentRepository", "caught an exception $e")
            return Failure("Network/server problem")
        }
    }


    override fun getCommentByTaskId(taskId: Int?): Flow<List<Comment>?> {
        if(taskId != null)
        return commentDao.getCommentsByTaskIdFlow(taskId).transform { listComments ->
            Log.d("CommentRepository", "arranging comments" + listComments.toString())
            emit(arrangingComments(listComments).fromListCommentEntitiesToListComments())
        }
        return flow {}
    }

    override suspend fun putCommentToMessage(messageId: Int, comment: Comment): Result<String, String> {
        //Log.d("ProjectRepository", "Started creating pproject" + comment.toCommentPost().toString())

        return networkCaller(
            call = { commentEndPoint.putCommentToMessage(messageId, comment.toCommentPost()) },
            onSuccess = { },
            onSuccessString = "Comment added successfully",
            onFailureString = "Having problem while creating the comment, please check the network connection"
        )
    }

    override suspend fun putCommentToTask(taskId: Int, comment: Comment) : Result<String, String> {
        Log.d("CommentRepository", "Started creating comment $taskId")

        return networkCaller(
            call = { commentEndPoint.putCommentToTask(taskId, comment.toCommentPost()) },
            onSuccess = { },
            onSuccessString = "Comment added successfully",
            onFailureString = "Having problem while creating the comment, please check the network connection"
        )
    }

    override suspend fun deleteComment(commentId: String, taskId: Int?): Result<String, String> {

        return networkCaller(
            call = { commentEndPoint.deleteComment(commentId) },
            onSuccess = {
                if (taskId != null) {
                    populateCommentsWithTaskId(taskId)
                }
            },
            onSuccessString = "Comment added successfully",
            onFailureString = "Having problem while вулуештп the comment, please check the network connection"
        )
    }
}