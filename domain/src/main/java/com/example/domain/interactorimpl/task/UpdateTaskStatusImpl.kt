package com.example.domain.interactorimpl.task

import com.example.domain.filters.task.TaskStatus
import com.example.domain.interactor.task.UpdateTaskStatus
import com.example.domain.mappers.fromTaskStatusToString
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result

class UpdateTaskStatusImpl(
    private val taskRepository: TaskRepository
) : UpdateTaskStatus {
    override suspend operator fun invoke(taskId: Int?, status: TaskStatus?): Result<String, String> {
        if(taskId == null) {
            return Failure("can't figure current task id, please reload the page")
        }
        if (status == null) {
            return Failure("can't figure current task status, please reload the page")
        }

        return taskRepository.updateTaskStatus(
            taskId,
            status.fromTaskStatusToString()
        )
    }
}