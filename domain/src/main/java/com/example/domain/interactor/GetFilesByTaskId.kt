package com.example.domain.interactor

import com.example.domain.model.File
import com.example.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow

class GetFilesByTaskId(
    private val fileRepository: FileRepository
) {

    operator fun invoke(taskId: Int): Flow<List<File>> {
            //fileRepository.populateTaskFiles(taskId)
            return fileRepository.getFilesWithTaskId(taskId)
    }
}