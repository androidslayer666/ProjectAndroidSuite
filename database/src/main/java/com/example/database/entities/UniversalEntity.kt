package com.example.database.entities

sealed class UniversalEntity(
    open val id: Int,
    open val title: String? = null,
)