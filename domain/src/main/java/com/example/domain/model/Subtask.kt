package com.example.domain.model

data class Subtask(
    val canEdit: Boolean? = null,
    val taskId: Int,
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    val status: Int? = null,
    val responsible: User? = null,
    val updatedBy: User? = null,
    val created: String? = null,
    val createdBy: User? = null,
    val updated: String? = null

)



