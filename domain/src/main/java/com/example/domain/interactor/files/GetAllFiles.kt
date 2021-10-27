package com.example.domain.interactor.files

import com.example.domain.FileFilter
import com.example.domain.filterFileByFilter
import com.example.domain.model.File
import com.example.domain.repository.FileRepository
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GetAllFiles(
    private val fileRepository: FileRepository,
    private val projectRepository: ProjectRepository
) {

    private var fileFilter = MutableStateFlow<FileFilter?>(null)

    operator fun invoke(): Flow<List<File>> {
        CoroutineScope(Dispatchers.IO).launch {
            projectRepository.getAllStoredProjects().collectLatest { projects ->
                projects.forEach {
                    fileRepository.populateProjectFiles(it.id)
                }
            }
        }

        return fileRepository.getAllFiles().combine(fileFilter) { files, filter ->
            files.filterFileByFilter(filter)
        }
    }

    fun setFilter(searchQuery: String? = null) {
        fileFilter.value = FileFilter(searchQuery)
    }
}