package com.example.domain.fakes.filters

import com.example.domain.fakes.DummyDateList
import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Task
import com.example.domain.model.User

object DummyListTaskFilter {
    val list = listOf(
        Task(
            id = 0,
            status = TaskStatus.COMPLETE,
            deadline = DummyDateList.listDates[0]!!,
            priority = 0,
        ),
        Task(
            id = 1,
            status = TaskStatus.COMPLETE,
            deadline = DummyDateList.listDates[1]!!,
            priority = 0,
            title = "title"
        ),
        Task(
            id = 2,
            status = TaskStatus.COMPLETE,
            deadline = DummyDateList.listDates[2]!!,
            priority = 0,
            responsibles = mutableListOf(User(id = "111"))
        ),
        Task(
            id = 3,
            status = TaskStatus.ACTIVE,
            deadline = DummyDateList.listDates[3]!!,
            priority = 1
        ),
    )
}