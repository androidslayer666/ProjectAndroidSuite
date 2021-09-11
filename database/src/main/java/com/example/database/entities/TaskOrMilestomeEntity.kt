package com.example.database.entities

sealed class TaskOrMilestoneEntity(
    open val id: Int,
    open val title: String? = null,
    open val description: String? = null,
)