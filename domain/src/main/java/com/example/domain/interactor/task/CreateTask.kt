package com.example.domain.interactor.task

import android.util.Log
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.Failure
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(milestoneId: Int?, task: Task): Result<String, String> {
        Log.d("CreateTask", task.toString())
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasks()
        }
        return task.projectId?.let { taskRepository.createTask(milestoneId, task, it) }
            ?: Failure("can't figure out to which project attach the task ")
    }
}