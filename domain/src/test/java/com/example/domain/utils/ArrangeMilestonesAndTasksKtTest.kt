package com.example.domain.utils

import com.example.domain.fakes.utils.DummyListMilestonesForMap
import com.example.domain.fakes.utils.DummyListTasksForMap
import com.example.domain.filters.task.TaskStatus
import com.example.domain.model.Milestone
import com.example.domain.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArrangeMilestonesAndTasksKtTest {

    private val finalMap = mapOf(
        Milestone(id = 111) to listOf(
            Task(id = 1, milestoneId = 111, status = TaskStatus.ACTIVE),
            Task(id = 3, milestoneId = 111, status = TaskStatus.ACTIVE)
        ),
        Milestone(id = 222) to listOf(
            Task(id = 2, milestoneId = 222, status = TaskStatus.ACTIVE),
            Task(id = 4, milestoneId = 222, status = TaskStatus.ACTIVE),
        ),
        null to listOf(Task(id = 5, status = TaskStatus.ACTIVE))
    )

    @Test
    fun testArrangeMilestonesAndTasksKt_KeysAreEquals(){
        val mapFromFun = arrangeMilestonesAndTasks(DummyListMilestonesForMap.list, DummyListTasksForMap.list)
        finalMap.keys.forEach { key ->
            assertEquals(finalMap[key]?.size,mapFromFun[key]?.size)
        }
    }

    @Test
    fun testArrangeMilestonesAndTasksKt_SizesOfListAreEquals(){
        val mapFromFun = arrangeMilestonesAndTasks(DummyListMilestonesForMap.list, DummyListTasksForMap.list)
        assertTrue(finalMap.keys == mapFromFun.keys)
    }
}