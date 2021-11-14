package com.example.domain.utils

import com.example.domain.Constants
import com.example.domain.fakes.utils.DummyCommentList
import org.junit.Assert.assertEquals
import org.junit.Test

class ArrangingCommentsKtTest {

    private val processedList = arrangingComments(DummyCommentList.list)

    @Test
    fun arrangingComments_sizeOfFinalListIsTheSameAsInitial() {
        assertEquals(DummyCommentList.list.size, processedList.size)
    }

    @Test
    fun arrangingComments_nextCommentHasParentIdOfPrevious() {
        if (processedList[1].parentId != Constants.COMMENT_ID_ALL_NULLS) {
            assertEquals(processedList[1].parentId, processedList[0].id)
        }
    }
}