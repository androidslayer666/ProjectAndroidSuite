package com.example.domain.interactorimpl.task

import com.example.domain.fakes.repository.FakeTaskRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetAllTasksTest{

    val getAllTasks = GetAllTasks(FakeTaskRepository())

    @Test
    fun getAllTheTasksThatRepoEmits() {
        runBlockingTest {
            val list = getAllTasks().first()
            assertEquals(1, list.size)
        }
    }

}