package com.example.domain.utils

import com.example.domain.Constants
import com.example.domain.entities.CommentEntity
import com.example.domain.model.Comment

fun arrangingComments(list: List<Comment>): List<Comment> {
    val childList = mutableListOf<Comment>()
    val parentList = mutableListOf<Comment>()
    for (comment in list) {
        if (comment.parentId != Constants.COMMENT_ID_ALL_NULLS) {
            childList.add(comment)
        } else {
            parentList.add(comment)
        }
    }
    return insertingCommentToParent(parentList, childList)
}

fun insertingCommentToParent(
    parentList: MutableList<Comment>,
    childList: MutableList<Comment>,
    commentLevel: Int = 1
): List<Comment> {

    var counter = childList.size

    for (i in 0 until parentList.size) {
        for (y in 0 until counter) {

            if (counter == (y)) {
                break
            }
            if (childList[y].parentId == parentList[i].id) {
                if (parentList.indexOf(parentList[i]) != parentList.size - 1) {
                    childList[y].commentLevel = commentLevel
                    parentList.add(parentList.indexOf(parentList[i]) + 1, childList[y])
                } else {
                    childList[y].commentLevel = commentLevel
                    parentList.add(childList[y])
                }
                childList.remove(childList[y])
                counter -= 1
            }
        }
    }
    if (childList.size > 0) {
        insertingCommentToParent(parentList, childList, commentLevel + 1)
    } else {
        return parentList
    }
    return parentList
}