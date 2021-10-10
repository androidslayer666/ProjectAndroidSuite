package com.example.domain.model

import java.util.*


data class Message(
    val canCreateComment: Boolean? = null,
    val canEdit: Boolean? = null,
    val id: Int,
    var title: String ="",
    val description: String? = null,
    val projectOwner: User? = null,
    val commentsCount: Int? = null,
    val text: String = "",
    val status: Int? = null,
    val updatedBy: User? = null,
    val created: Date = Date(),
    val createdBy: User? = null,
    val updated: Date = Date(),
    val projectId: Int? = null,
    var listMessages: List<Comment>? = null
)
