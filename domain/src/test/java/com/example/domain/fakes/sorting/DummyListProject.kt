package com.example.domain.fakes.sorting

import com.example.domain.fakes.DummyDateList
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.model.Project

object DummyListProject {

    val list = listOf(
        Project(
            id=0,
            status = ProjectStatus.ACTIVE,
            created = DummyDateList.listDates[0]!!
        ),
        Project(
            id=1,
            status = ProjectStatus.PAUSED,
            created = DummyDateList.listDates[1]!!
        ),
        Project(
            id=2,
            status = ProjectStatus.STOPPED,
            created = DummyDateList.listDates[2]!!
        ),
        Project(
            id=3,
            status = ProjectStatus.ACTIVE,
            created = DummyDateList.listDates[3]!!
        ),

    )
}