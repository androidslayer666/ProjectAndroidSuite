package com.example.domain.mappers

import com.example.database.entities.MessageEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.toStringIds
import com.example.network.dto.MessageDto
import com.example.network.dto.MessagePost
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

fun MessageEntity.toMessagePost(participants: List<UserEntity>): MessagePost {
    return MessagePost(
        title = this.title,
        content = this.description,
    participants = participants.toStringIds()
    )
}