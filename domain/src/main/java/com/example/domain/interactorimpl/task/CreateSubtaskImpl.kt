package com.example.domain.interactorimpl.task

import com.example.domain.interactor.task.CreateSubtask
import com.example.domain.model.Subtask
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Result
import com.example.domain.utils.toStringString

class CreateSubtaskImpl(
    private val taskRepository: TaskRepository
) : CreateSubtask {
    override suspend operator fun invoke(subtask: Subtask): Result<String, String> {
        return taskRepository.createSubtask(subtask).toStringString()
    }
}