package com.example.domain.interactorimpl.file

import com.example.domain.interactor.file.GetFilesByProjectId
import com.example.domain.model.File
import com.example.domain.repository.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GetFilesByProjectIdImpl(
    private val fileRepository: FileRepository
) : GetFilesByProjectId {
    override operator fun invoke(projectId: Int?): Flow<List<File>> {
        return projectId?.let {
            CoroutineScope(Dispatchers.IO).launch {
                fileRepository.populateProjectFiles(it)
            }
            fileRepository.getFilesWithProjectId(it)
        } ?: flow {}
    }
}