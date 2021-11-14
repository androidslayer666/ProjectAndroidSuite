package com.example.domain.interactor.file

import com.example.domain.model.File
import kotlinx.coroutines.flow.Flow

interface GetFilesByTaskId {
    operator fun invoke(taskId: Int): Flow<List<File>>
}