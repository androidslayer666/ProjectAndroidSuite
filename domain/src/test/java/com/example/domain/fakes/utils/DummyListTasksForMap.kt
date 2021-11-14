package com.example.domain.fakes.utils

import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Task

object DummyListTasksForMap {

    val list = listOf(
        Task(id = 1, milestoneId = 111, status = TaskStatus.ACTIVE),
        Task(id = 2, milestoneId = 222, status = TaskStatus.ACTIVE),
        Task(id = 3, milestoneId = 111, status = TaskStatus.ACTIVE),
        Task(id = 4, milestoneId = 222, status = TaskStatus.ACTIVE),
        Task(id = 5, status = TaskStatus.ACTIVE)
    )
}