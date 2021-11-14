package com.example.domain.sorting

import com.example.domain.fakes.filters.DummyListTaskFilter
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskSortingKtTest{

    @Test
    fun taskSorting_sortByStageAsc() {
        val sorted = DummyListTaskFilter.list.sortTasks(TaskSorting.STAGE_ASC)
        println(sorted)
        assertEquals(0, sorted[0].id)
    }

    @Test
    fun taskSorting_sortByStageDesc() {
        val sorted = DummyListTaskFilter.list.sortTasks(TaskSorting.STAGE_DESC)
        println(sorted)
        assertEquals(1, sorted[0].id)
    }

    @Test
    fun taskSorting_sortByDeadlineAsc() {
        val sorted = DummyListTaskFilter.list.sortTasks(TaskSorting.DEADLINE_ASC)
        println(sorted)
        assertEquals(0, sorted[0].id)
    }

    @Test
    fun taskSorting_sortByDeadlineDesc() {
        val sorted = DummyListTaskFilter.list.sortTasks(TaskSorting.DEADLINE_DESC)
        println(sorted)
        assertEquals(3, sorted[0].id)
    }


    @Test
    fun taskSorting_sortByPriorityAsc() {
        val sorted = DummyListTaskFilter.list.sortTasks(TaskSorting.IMPORTANT_ASC)
        println(sorted)
        assertEquals(0, sorted[0].id)
    }

    @Test
    fun taskSorting_sortByPriorityDesc() {
        val sorted = DummyListTaskFilter.list.sortTasks(TaskSorting.IMPORTANT_DESC)
        println(sorted)
        assertEquals(3, sorted[0].id)
    }

}