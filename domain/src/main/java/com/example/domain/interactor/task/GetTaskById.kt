package com.example.domain.interactor.task

import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskById(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Int): Flow<Task?> {
        return taskRepository.getTaskById(taskId)
    }
}