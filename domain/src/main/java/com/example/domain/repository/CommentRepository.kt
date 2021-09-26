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
            Log.d("CommentRepository", comments.toString())

            return if (comments == null) {
                Failure("Can't download comments")
            } else {
                arrangingComments(comments.toListEntities(taskId)).let {
                    commentDao.insertComments(it)
                }
                commentDao.getCommentsByTaskId(taskId).forEach {
                    Log.d("CommentRepository", "id " + it.id)
                    if(comments?.toListCommentIds()?.contains(it.id) != true){
                        Log.d("CommentRepository", "DeleteMessage with id " + it.id)
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
        try {
            val response = commentEndPoint.putCommentToMessage(messageId, comment.toCommentPost())
            //Log.d("ProjectRepository", response.toString())
            if (response != null) {
                Log.d("ProjectRepository", "Comment created")
                //because response from server doesn't give id
                //getProjects()
                return Success("Comment successfully create")
            } else {
                return Failure("Comment was not created due to a server problem")
            }
        } catch (e: Exception) {
//            Log.d(
//                "ProjectRepository",
//                "tried to create a Comment but caught an exception: ${e.message}"
//            )
            return Failure("Comment was not created, please check network or ask the developer to fix this")
        }
    }

    suspend fun putCommentToTask(taskId: Int, comment: CommentEntity) : Result<String, String> {
        Log.d("CommentRepository", "Started creating comment" + taskId.toString())
        try {
            val response = commentEndPoint.putCommentToTask(taskId, comment.toCommentPost())
            //Log.d("ProjectRepository", response.toString())
            if (response != null) {
                Log.d("ProjectRepository", "Comment created")
                //because response from server doesn't give id
                //getProjects()
                return Success("Comment successfully create")
            } else {
                return Failure("Comment was not created due to a server problem")
            }
        } catch (e: Exception) {
            Log.d(
                "CommentRepository",
                "tried to create a Comment but caught an exception: ${e.message}"
            )
            return Failure("Comment was not created, please check network or ask the developer to fix this")
        }
    }

    suspend fun deleteComment(commentId: String, taskId: Int): Result<String, String> {
        try {
            Log.d("TaskRepository", "Start deleting the comment with id : $commentId")
            val response = commentEndPoint.deleteComment(commentId)
            if (response != null) {
                Log.d("CommentRepository", "Task deleted")
                populateCommentsWithTaskId(taskId)
                return Success("The comment successfully deleted")
            } else {
                Log.d("CommentRepository", "Failed to delete the comment")
                return Failure("Unable to delete the task")
            }
        } catch (e: Exception) {
            Log.d("CommentRepository", "tried to create a comment but got an exception ${e.message}")
            return Failure("Unable to delete the comment")
        }
    }


}