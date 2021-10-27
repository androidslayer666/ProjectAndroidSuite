package com.example.domain.interactor

import com.example.domain.Result
import com.example.domain.model.Subtask
import com.example.domain.repository.TaskRepository

class CreateSubtask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(subtask: Subtask): Result<String, String> {
        return taskRepository.createSubtask(subtask)
    }
}