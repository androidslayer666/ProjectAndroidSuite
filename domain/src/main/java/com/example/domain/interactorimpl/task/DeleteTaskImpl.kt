package com.example.domain.interactorimpl.task

import com.example.domain.interactor.task.DeleteTask
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.toStringString

class DeleteTaskImpl(
    private val taskRepository: TaskRepository
) : DeleteTask {
    override suspend operator fun invoke(taskId: Int?): Result<String, String> {
        return taskId?.let{
            taskRepository.deleteTask(taskId).toStringString()
        } ?: Failure("can't figure current task status, please reload the page")
    }

}