package com.example.domain.interactor.task

import com.example.domain.Failure
import com.example.domain.Result
import com.example.domain.repository.TaskRepository

class DeleteTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int?): Result<String, String> {
        return taskId?.let{
            taskRepository.deleteTask(taskId)
        } ?: Failure("can't figure current task status, please reload the page")
    }

}