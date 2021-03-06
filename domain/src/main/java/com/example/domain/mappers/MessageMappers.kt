package com.example.domain.mappers

import com.example.domain.dto.MessageDto
import com.example.domain.dto.MessagePost
import com.example.domain.entities.MessageEntity
import com.example.domain.model.Message
import com.example.domain.model.User
import java.util.*

fun List<MessageDto>.toListEntities(projectId: Int): List<MessageEntity> {
    val listCommentEntity = mutableListOf<MessageEntity>()
    for (messageDto in this) {
        listCommentEntity.add(
            messageDto.toEntity(projectId)
        )
    }
    return listCommentEntity
}

fun MessageDto.toEntity(projectId: Int): MessageEntity {
    return MessageEntity(
        id = this.id,
        canEdit = this.canEdit,
        created = this.created?.stringToDate() ?: Date(),
        createdBy = this.createdBy?.toUserEntity(),
        text = this.text ?: "",
        updated = this.updated?.stringToDate()?: Date(),
        canCreateComment = this.canCreateComment,
        commentsCount = this.commentsCount,
        projectId = projectId,
        description = this.description,
        projectOwner = this.projectOwner?.toUserEntity(),
        status = this.status,
        title = this.title ?: "",
        updatedBy = this.updatedBy?.toUserEntity()
    )
}

fun Message.toMessagePost(projectId: Int? = 0,participants: List<User>): MessagePost {
    return MessagePost(
        title = this.title,
        content = this.text,
        projectId = projectId,
        participants = participants.toStringIds()
    )
}


fun List<MessageEntity>.fromListMessageEntitiesToListMessages(): MutableList<Message> {
    val listMessages = mutableListOf<Message>()
    listMessages.addAll(this.map { it.fromMessageEntityToMessage() })
    return listMessages
}


fun MessageEntity.fromMessageEntityToMessage() : Message {
    return Message(
        id = this.id,
        canEdit = this.canEdit,
        created = this.created,
        createdBy = this.createdBy?.fromUserEntityToUser(),
        text = this.text,
        updated = this.updated,
        canCreateComment = this.canCreateComment,
        commentsCount = this.commentsCount,
        projectId = projectId,
        description = this.description,
        projectOwner = this.projectOwner?.fromUserEntityToUser(),
        status = this.status,
        title = this.title,
        updatedBy = this.updatedBy?.fromUserEntityToUser(),
        listMessages = this.listComments?.fromListCommentEntitiesToListComments()
    )
}

fun List<MessageDto>.toListMessageIds() : List<Int> {
    val listString = mutableListOf<Int>()
    for (message in this) {
        listString.add(message.id)
    }
    return listString
}