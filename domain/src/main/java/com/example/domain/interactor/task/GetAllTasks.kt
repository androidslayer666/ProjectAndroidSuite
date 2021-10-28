package com.example.domain.interactor.task

import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.repository.TaskRepository
import com.example.domain.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GetAllTasks(
    private val taskRepository: TaskRepository
) {

    private var taskFilter = MutableStateFlow<TaskFilter?>(null)

    private var taskSorting = MutableStateFlow(TaskSorting.STAGE_ASC)

    suspend operator fun invoke() : Flow<List<Task>> {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.populateTasks()
        }
        return taskRepository.getAllTasks().combine(taskFilter){ tasks, filter ->
            tasks.filterTaskByFilter(filter)
        }.combine(taskSorting) { tasks, sorting ->
            tasks.sortTasks(sorting)
        }
    }

    fun setFilter(searchQuery: String? = null, status: TaskStatus? = null, user: User? = null) {
        searchQuery?.let {
            taskFilter.value = TaskFilter(
                searchQuery = searchQuery,
                status = taskFilter.value?.status,
                responsible = taskFilter.value?.responsible
            )
        }
        status?.let {
            taskFilter.value = TaskFilter(
                searchQuery = taskFilter.value?.searchQuery,
                status = status,
                responsible = taskFilter.value?.responsible
            )
        }
        user?.let {
            taskFilter.value = TaskFilter(
                searchQuery = taskFilter.value?.searchQuery,
                status = taskFilter.value?.status,
                responsible = user
            )
        }
        if (searchQuery == null && status == null && user == null) {
            taskFilter.value = null
        }
    }

    fun setTaskSorting(sorting: TaskSorting) {
        taskSorting.value = sorting
    }

}