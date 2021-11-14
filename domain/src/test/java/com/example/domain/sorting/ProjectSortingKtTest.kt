package com.example.domain.sorting

import com.example.domain.fakes.filters.DummyListProjectFilter
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectSortingKtTest{

    @Test
    fun projectSorting_sortByStageAsc(){
        val sorted = DummyListProjectFilter.list.sortProjects(ProjectSorting.STAGE_ASC)
        println(sorted)
        assertEquals(0, sorted[0].id)
    }

    @Test
    fun projectSorting_sortByStageDesc(){
        val sorted = DummyListProjectFilter.list.sortProjects(ProjectSorting.STAGE_DESC)
        println(sorted)
        assertEquals(2, sorted[0].id)
    }

    @Test
    fun projectSorting_sortByDateAsc(){
        val sorted = DummyListProjectFilter.list.sortProjects(ProjectSorting.CREATED_ASC)
        println(sorted)
        assertEquals(0, sorted[0].id)
    }

    @Test
    fun projectSorting_sortByDateDesc(){
        val sorted = DummyListProjectFilter.list.sortProjects(ProjectSorting.CREATED_DESC)
        println(sorted)
        assertEquals(3, sorted[0].id)
    }


}