package com.example.domain.interactorimpl.task

import com.example.domain.interactor.task.GetTaskByProjectId
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByProjectIdImpl(
    private val taskRepository: TaskRepository
) : GetTaskByProjectId {
    override operator fun invoke(projectId: Int): Flow<List<Task>> {
        return taskRepository.getTasksByProject(projectId)
    }
}