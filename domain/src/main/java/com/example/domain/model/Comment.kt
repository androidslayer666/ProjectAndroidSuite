package com.example.domain.model

data class Comment(
    val id: String,
    val createdBy: User? = null,
    val text: String = "",
    val parentId: String? = null,
    val inactive: Boolean? = null,
    val canEdit: Boolean? = null,
    val taskId: Int? = null,
    val messageId: Int? = null,
    var commentLevel: Int  = 0
)



