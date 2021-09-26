package com.example.domain.mappers

import com.example.database.entities.CommentEntity
import com.example.network.dto.CommentDto
import com.example.network.dto.CommentPost
import java.util.*

fun List<CommentDto>.toListEntities(
    taskId: Int? = null,
    messageId: Int? = null
): List<CommentEntity> {
    val listCommentEntity = mutableListOf<CommentEntity>()
    for (commentDto in this) {
        listCommentEntity.add(
            commentDto.toEntity(taskId, messageId)
        )
    }
    return listCommentEntity
}

fun CommentDto.toEntity(
    taskId: Int? = null,
    messageId: Int? = null
): CommentEntity {
    return CommentEntity(
        id = this.id,
        canEdit = this.canEdit,
        created = this.created?.stringToDate() ?: Date(),
        createdBy = this.createdBy?.toUserEntity(),
        inactive = this.inactive,
        parentId = this.parentId,
        text = this.text ?: "",
        updated = this.updated?.stringToDate() ?: Date(),
        taskId = taskId,
        messageId = messageId

    )
}

fun CommentEntity.toCommentPost(): CommentPost {
return CommentPost(
    content = this.text,
    parentId = this.parentId
)
}