package com.example.domain.filters.project

import com.example.domain.fakes.filters.DummyListProjectFilter
import com.example.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectFilterKtTest {

    @Test
    fun filterProjectsByFilter_filterTitle() {
        val filteredProjects = DummyListProjectFilter.list.filterProjectsByFilter(ProjectFilter("title"))
        assertEquals(1, filteredProjects[0].id)
    }

    @Test
    fun filterProjectsByFilter_filterStage() {
        val filteredProjects = DummyListProjectFilter.list.filterProjectsByFilter(ProjectFilter(status = ProjectStatus.ACTIVE))
        assertEquals(3, filteredProjects[0].id)
    }

    @Test
    fun filterProjectsByFilter_filterFilterResponsible() {
        val filteredProjects = DummyListProjectFilter.list.filterProjectsByFilter(ProjectFilter(responsible = User(id = "111")))
        assertEquals(2, filteredProjects[0].id)
    }


}