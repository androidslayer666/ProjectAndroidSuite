package com.example.domain.interactorimpl.task

import com.example.domain.filters.task.TaskFilter
import com.example.domain.filters.task.TaskStatus
import com.example.domain.filters.task.filterTaskByFilter
import com.example.domain.interactor.task.GetOverdueTasks
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.util.*

class GetOverdueTasksImpl(
    private val taskRepository: TaskRepository
) : GetOverdueTasks {

    override suspend fun invoke(): Flow<List<Task>> {

        val calendar = Calendar.getInstance()

        // Taking 100 days interval to hide very old tasks
        calendar.add(Calendar.DAY_OF_MONTH, -100)

        return taskRepository.getAllTasks().transform { tasks ->
            emit(
                tasks.filterTaskByFilter(
                    TaskFilter(
                        interval = Pair(
                            calendar.time,
                            Date()
                        ),
                        status = TaskStatus.ACTIVE
                    )
                )
            )
        }
    }
}