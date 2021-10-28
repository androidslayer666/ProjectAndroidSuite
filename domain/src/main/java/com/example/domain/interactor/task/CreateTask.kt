package com.example.domain.interactor.task

import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import com.example.domain.utils.Success
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(milestoneId: Int?, task: Task): Result<String, String> {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasks()
        }
        return taskRepository.createTask(milestoneId, task)
    }
}