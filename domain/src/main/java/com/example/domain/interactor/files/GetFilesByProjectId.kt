package com.example.domain.interactor.files

import com.example.domain.model.File
import com.example.domain.repository.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetFilesByProjectId(
    private val fileRepository: FileRepository
) {
    operator fun invoke(projectId: Int?): Flow<List<File>> {
        CoroutineScope(Dispatchers.IO).launch {
            fileRepository.populateProjectFiles(projectId?:0)
        }
        return fileRepository.getFilesWithProjectId(projectId?:0)
    }
}