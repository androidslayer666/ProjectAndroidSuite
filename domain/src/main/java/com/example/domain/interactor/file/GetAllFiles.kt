package com.example.domain.interactor.file

import com.example.domain.model.File
import kotlinx.coroutines.flow.Flow

interface GetAllFiles {
    operator fun invoke(): Flow<List<File>>
    fun setFilter(searchQuery: String? = null)
}