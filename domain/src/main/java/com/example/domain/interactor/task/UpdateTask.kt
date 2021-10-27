package com.example.domain.interactor.task

import com.example.domain.Failure
import com.example.domain.Result
import com.example.domain.TaskStatus
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int?, task: Task, taskStatus: TaskStatus?): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasks()
        }

        return taskId?.let {
            taskRepository.updateTask(
                taskId,
                task,
                when (taskStatus) {
                    TaskStatus.ACTIVE -> "Open"
                    TaskStatus.COMPLETE -> "Closed"
                    else -> "Open"
                })
        }?: Failure("Can't figure out the task id, please reload the page")
    }
}