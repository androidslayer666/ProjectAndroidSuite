package com.example.domain.interactorimpl.comment


import com.example.domain.fakes.repository.FakeCommentRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


class GetCommentByTaskIdTest {

    val getCommentByTaskId = GetCommentByTaskId(FakeCommentRepository())

    @Test
    fun getCommentByTaskId_gettingTheSameCommentThatRepoEmits() {
        runBlockingTest{
            val listComments = getCommentByTaskId(1).first()
            assertEquals(listComments?.get(0)?.text, "Fake comment")
        }
    }




}