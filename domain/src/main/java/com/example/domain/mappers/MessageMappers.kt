package com.example.domain.mappers

import com.example.database.entities.MessageEntity
import com.example.network.dto.MessageDto

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
        created = this.created?.stringToDate(),
        createdBy = this.createdBy?.toUserEntity(),
        text = this.text,
        updated = this.updated?.stringToDate(),
        canCreateComment = this.canCreateComment,
        commentsCount = this.commentsCount,
        projectId = projectId,
        description = this.description,
        projectOwner = this.projectOwner?.toUserEntity(),
        status = this.status,
        title = this.title,
        updatedBy = this.updatedBy?.toUserEntity()

    )
}