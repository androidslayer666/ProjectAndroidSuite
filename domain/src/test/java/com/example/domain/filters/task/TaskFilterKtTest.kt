package com.example.domain.filters.task

import com.example.domain.fakes.DummyDateList
import com.example.domain.fakes.filters.DummyListTaskFilter
import com.example.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskFilterKtTest {

    @Test
    fun taskFilter_filterSearchQuery( ) {
        val filtered = DummyListTaskFilter.list.filterTaskByFilter(TaskFilter("title"))
        assertEquals(1, filtered[0].id)
    }

    @Test
    fun taskFilter_filterResponsible( ) {
        val filtered = DummyListTaskFilter.list.filterTaskByFilter(TaskFilter(responsible = User(id ="111")))
        assertEquals(2, filtered[0].id)
    }

    @Test
    fun taskFilter_filterStage( ) {
        val filtered = DummyListTaskFilter.list.filterTaskByFilter(TaskFilter(status = TaskStatus.ACTIVE))
        assertEquals(3, filtered[0].id)
    }

    @Test
    fun taskFilter_filterStartDate( ) {
        val filtered = DummyListTaskFilter.list.filterTaskByFilter(TaskFilter(interval = Pair(DummyDateList.listDates[1],null)))
        assertEquals(1, filtered[0].id)
    }

    @Test
    fun taskFilter_filterEndDate( ) {
        val filtered = DummyListTaskFilter.list.filterTaskByFilter(TaskFilter(interval = Pair(null, DummyDateList.listDates[2])))
        assertEquals(3, filtered.size)
    }

}