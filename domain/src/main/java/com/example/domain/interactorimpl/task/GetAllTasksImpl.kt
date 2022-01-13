package com.example.domain.interactorimpl.task

import com.example.domain.filters.task.TaskFilter
import com.example.domain.filters.task.TaskStatus
import com.example.domain.filters.task.filterTaskByFilter
import com.example.domain.interactor.task.GetAllTasks
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.repository.TaskRepository
import com.example.domain.sorting.TaskSorting
import com.example.domain.sorting.sortTasks
import com.example.domain.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*

class GetAllTasksImpl(
    private val taskRepository: TaskRepository
) : GetAllTasks {

    private var taskFilter = MutableStateFlow<TaskFilter?>(TaskFilter())

    private var taskSorting = MutableStateFlow(TaskSorting.STAGE_ASC)

    override suspend operator fun invoke(): Flow<List<Task>> {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasks()
        }
        return taskRepository.getAllTasks().combine(taskFilter) { tasks, filter ->
            tasks.filterTaskByFilter(filter)
        }.combine(taskSorting) { tasks, sorting ->
            tasks.sortTasks(sorting)
        }
    }

    override fun setFilter(
        searchQuery: String?,
        status: TaskStatus?,
        user: User?,
        interval: Pair<Date?, Date?>?
    ) {
        if(searchQuery != null){
            taskFilter.value = taskFilter.value?.copy(
                searchQuery = searchQuery,
            )
        }
        if(status != null){
            taskFilter.value = taskFilter.value?.copy(
                status = status,
            )
        }
        if(user != null){
            taskFilter.value = taskFilter.value?.copy(
                responsible = user
            )
        }
        if(interval != null) {
            taskFilter.value = taskFilter.value?.copy(
                interval = interval
            )
        }

        if (searchQuery == null && status == null && user == null) {
            taskFilter.value = null
        }

        log(taskFilter.value)
    }

    override fun setTaskSorting(sorting: TaskSorting) {
        taskSorting.value = sorting
    }

}