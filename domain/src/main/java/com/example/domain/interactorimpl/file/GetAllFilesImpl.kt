package com.example.domain.interactorimpl.file


import com.example.domain.filters.file.FileFilter
import com.example.domain.filters.file.filterFileByFilter
import com.example.domain.interactor.file.GetAllFiles
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

class GetAllFilesImpl(
    private val fileRepository: FileRepository,
    private val projectRepository: ProjectRepository
) : GetAllFiles {

    private var fileFilter = MutableStateFlow<FileFilter?>(null)

    override operator fun invoke(): Flow<List<File>> {
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

    override fun setFilter(searchQuery: String?) {
        fileFilter.value = FileFilter(searchQuery)
    }
}