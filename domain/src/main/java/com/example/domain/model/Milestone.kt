package com.example.domain.model

import java.util.*


data class Milestone(
    val canEdit: Boolean? = null,
    val canDelete: Boolean? = null,
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    val projectOwner: User? = null,
    val deadline: Date? = null,
    val isKey: Boolean? = null,
    val isNotify: Boolean? = null,
    val activeTaskCount: Int? = null,
    val closedTaskCount: Int? = null,
    val status: Int? = null,
    val responsible: User? = null,
    val updatedBy: User? = null,
    val created: Date? = null,
    val createdBy: User? = null,
    val updated: Date? = null,
    val projectId: Int? = null
)



