package com.example.data.endpoints

import com.example.domain.dto.MessageDto
import com.example.domain.dto.MessagePost
import com.example.domain.dto.MessageTransporter
import com.example.domain.dto.MessagesTransporter
import retrofit2.http.*

interface MessageEndPoint {

    @GET("api/2.0/project/{projectId}/message")
    suspend fun getMessagesWithProject(@Path("projectId")projectId: Int): MessagesTransporter

    @POST("api/2.0/project/{projectId}/message")
    suspend fun putMessageToProject(@Path("projectId")projectId: Int, @Body messageDto: MessagePost) : MessageDto

    @PUT("api/2.0/project/message/{messageId}")
    suspend fun updateMessage(@Path("messageId")messageId: Int, @Body messageDto: MessagePost): MessageDto

    @DELETE("api/2.0/project/message/{messageId}")
    suspend fun deleteMessage(@Path("messageId")messageId: Int): MessageDto

    @GET("api/2.0/project/message/{messageid}")
    suspend fun getMessageWithId(@Path("messageid")messageId: Int): MessageTransporter



}
