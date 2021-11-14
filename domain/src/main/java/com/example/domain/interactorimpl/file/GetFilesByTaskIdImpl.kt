package com.example.domain.interactorimpl.file

import com.example.domain.interactor.file.GetFilesByTaskId
import com.example.domain.model.File
import com.example.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow

class GetFilesByTaskIdImpl(
    private val fileRepository: FileRepository
) : GetFilesByTaskId {

    override operator fun invoke(taskId: Int): Flow<List<File>> {
            //fileRepository.populateTaskFiles(taskId)
            return fileRepository.getFilesWithTaskId(taskId)
    }
}