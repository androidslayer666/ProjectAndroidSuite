package com.example.domain.interactor.task

import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Task
import com.example.domain.model.User
import com.example.domain.sorting.TaskSorting
import kotlinx.coroutines.flow.Flow
import java.util.*

interface GetAllTasks {
    suspend operator fun invoke() : Flow<List<Task>>
    fun setFilter(searchQuery: String? = null, status: TaskStatus? = null, user: User? = null,
                  interval: Pair<Date?, Date?>? = null)
    fun setTaskSorting(sorting: TaskSorting)
    }