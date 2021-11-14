package com.example.domain.fakes.filters

import com.example.domain.fakes.DummyDateList
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.model.Project
import com.example.domain.model.User

object DummyListProjectFilter {

    val list = listOf(
        Project(
            id=0,
            status = ProjectStatus.PAUSED,
            created = DummyDateList.listDates[0]!!
        ),
        Project(
            id=1,
            status = ProjectStatus.PAUSED,
            created = DummyDateList.listDates[1]!!,
            title = "title"
        ),
        Project(
            id=2,
            status = ProjectStatus.STOPPED,
            created = DummyDateList.listDates[2]!!,
            responsible = User(id = "111")
        ),
        Project(
            id=3,
            status = ProjectStatus.ACTIVE,
            created = DummyDateList.listDates[3]!!
        ),

    )
}