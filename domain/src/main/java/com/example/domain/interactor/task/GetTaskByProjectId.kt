package com.example.domain.interactor.task

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByProjectId(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(projectId: Int): Flow<List<Task>> {
        return taskRepository.getTasksByProject(projectId)
    }
}