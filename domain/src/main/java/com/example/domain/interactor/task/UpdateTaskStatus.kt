package com.example.domain.interactor.task

import com.example.domain.Failure
import com.example.domain.Result
import com.example.domain.repository.TaskRepository

class UpdateTaskStatus(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int?, status: Int?): Result<String, String> {
        if(taskId == null) {
            return Failure("can't figure current task id, please reload the page")
        }
        if (status == null) {
            return Failure("can't figure current task status, please reload the page")
        }

        //todo extract status to int logic
        return taskRepository.updateTaskStatus(
            taskId,
            when (status) {
                1 -> "closed"
                2 -> "open"
                else -> "open"
            }
        )
    }
}