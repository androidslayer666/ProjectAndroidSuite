package com.example.domain.repository

import android.util.Log
import com.example.database.dao.CommentDao
import com.example.database.entities.CommentEntity
import com.example.domain.mappers.toCommentPost
import com.example.domain.mappers.toListEntities
import com.example.network.endpoints.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentEndPoint: CommentEndPoint,
    private val commentDao: CommentDao
) {

    suspend fun populateCommentsWithTaskId(taskId: Int): Result<String, String> {
        try {
            //Log.d("CommentRepository", taskId.toString())
            val comments = commentEndPoint.getTaskComment(taskId).listCommentDtos
            //Log.d("CommentRepository", comments.toString())

            return if (comments == null) {
                Failure("Can't download comments")
            } else {
                arrangingComments(comments.toListEntities(taskId)).let {
                    commentDao.insertComments(it)
                }
                commentDao.getCommentsByTaskId(taskId).forEach {
                    //Log.d("CommentRepository", "id " + it.id)
                    if(comments?.toListCommentIds()?.contains(it.id) != true){
                        //Log.d("CommentRepository", "DeleteMessage with id " + it.id)
                        commentDao.deleteComment(it.id)
                    }
                }
                Success("Comments are populated")
            }
        } catch (e: Exception) {
            Log.e("CommentRepository", "caught an exception" + e.toString())
            return Failure("Network/server problem")
        }
    }


    fun getCommentByTaskId(taskId: Int?): Flow<List<CommentEntity>?> {
        if(taskId != null)
        return commentDao.getCommentsByTaskIdFlow(taskId).transform { listComments ->
            //Log.d("CommentRepository", "arranging comments" + listComments.toString())
            emit(arrangingComments(listComments))
        }
        return flow{}
    }

    suspend fun putCommentToMessage(messageId: Int, comment: CommentEntity): Result<String, String> {
        //Log.d("ProjectRepository", "Started creating pproject" + comment.toCommentPost().toString())

        return networkCaller(
            call = { commentEndPoint.putCommentToMessage(messageId, comment.toCommentPost()) },
            onSuccess = {  },
            onSuccessString = "Comment added successfully",
            onFailureString = "Having problem while creating the comment, please check the network connection"
        )
    }

    suspend fun putCommentToTask(taskId: Int, comment: CommentEntity) : Result<String, String> {
        Log.d("CommentRepository", "Started creating comment" + taskId.toString())

        return networkCaller(
            call = { commentEndPoint.putCommentToTask(taskId, comment.toCommentPost()) },
            onSuccess = {  },
            onSuccessString = "Comment added successfully",
            onFailureString = "Having problem while creating the comment, please check the network connection"
        )
    }

    suspend fun deleteComment(commentId: String, taskId: Int? = null): Result<String, String> {

        //todo is it necessary to be nullable?
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