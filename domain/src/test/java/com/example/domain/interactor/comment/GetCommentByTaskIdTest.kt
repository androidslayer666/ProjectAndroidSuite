package com.example.domain.interactor.comment

import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test



class GetCommentByTaskIdTest : TestCase() {

    val getCommentByTaskId = GetCommentByTaskId(FakeRepository())

    @Test
    fun getComment() {
        runBlockingTest{
            val listComments = getCommentByTaskId(1).first()
            assertEquals(listComments?.get(0)?.text, "Fake comment")
        }
    }


}