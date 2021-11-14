package com.example.domain.interactorimpl.task

import com.example.domain.interactor.task.GetTaskById
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByIdImpl(
    private val taskRepository: TaskRepository
) : GetTaskById {
    override operator fun invoke(taskId: Int): Flow<Task?> {
        return taskRepository.getTaskById(taskId)
    }
}