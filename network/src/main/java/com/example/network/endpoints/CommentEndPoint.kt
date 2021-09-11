package com.example.network.endpoints

import com.example.network.dto.CommentDto
import com.example.network.dto.CommentPost
import com.example.network.dto.CommentsTransporter
import retrofit2.http.*

interface CommentEndPoint {

    @GET("api/2.0/project/message/{messageId}/comment")
    suspend fun getMessageComments(@Path("messageId")messageId: Int): CommentsTransporter

    @GET("api/2.0/project/task/{taskId}/comment")
    suspend fun getTaskComment(@Path("taskId")taskId: Int): CommentsTransporter

    @POST("api/2.0/project/message/{messageId}/comment")
    suspend fun putCommentToMessage(@Path("messageId")messageId: Int, @Body comment: CommentPost): CommentDto

    @POST("api/2.0/project/task/{taskId}/comment")
    suspend fun putCommentToTask(@Path("taskId")taskId: Int, @Body comment: CommentPost): CommentDto

    @PUT("api/2.0/project/comment/{commentId}")
    suspend fun updateComment(@Path("commentId")commentId: Int, @Body comment: CommentPost): CommentDto

    @DELETE("api/2.0/project/comment/{commentId}")
    suspend fun deleteComment(@Path("commentId")commentId: Int, @Body comment: CommentPost): CommentDto

}