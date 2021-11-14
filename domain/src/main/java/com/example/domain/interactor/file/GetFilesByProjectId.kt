package com.example.domain.interactor.file

import com.example.domain.model.File
import kotlinx.coroutines.flow.Flow

interface GetFilesByProjectId {
    operator fun invoke(projectId: Int?): Flow<List<File>>
}