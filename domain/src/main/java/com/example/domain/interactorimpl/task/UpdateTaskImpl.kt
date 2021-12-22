package com.example.domain.interactorimpl.task

import com.example.domain.filters.task.TaskStatus
import com.example.domain.interactor.task.UpdateTask
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateTaskImpl(
    private val taskRepository: TaskRepository
) : UpdateTask {
    override suspend operator fun invoke(taskId: Int?, task: Task, taskStatus: TaskStatus?): Result<String, String> {
//        CoroutineScope(Dispatchers.IO).launch {
//            taskRepository.populateTasks()
//        }

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