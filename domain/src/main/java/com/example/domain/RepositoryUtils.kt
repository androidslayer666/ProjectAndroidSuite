package com.example.domain

import com.example.database.entities.CommentEntity
import com.example.domain.Constants.COMMENT_ID_ALL_NULLS

fun arrangingComments(list: List<CommentEntity>): List<CommentEntity> {
    val childList = mutableListOf<CommentEntity>()
    val parentList = mutableListOf<CommentEntity>()
    for (comment in list) {
        if (comment.parentId != COMMENT_ID_ALL_NULLS) {
            childList.add(comment)
        } else {
            parentList.add(comment)
        }
    }
    return insertingCommentToParent(parentList, childList)
}

fun insertingCommentToParent(
    parentList: MutableList<CommentEntity>,
    childList: MutableList<CommentEntity>,
    commentLevel: Int = 1
): List<CommentEntity> {

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

sealed class Result<out Success, out Failure>
data class Success<out Success>(val value: Success) : Result<Success, Nothing>()
data class Failure<out Failure>(val reason: Failure) : Result<Nothing, Failure>()


suspend fun <T> networkCaller(
    call: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onSuccessString: String = "",
    onFailureString: String = ""
): Result<String, String> {
    return try {
        val result = call()
        if (result != null) {
            onSuccess(result)
            Success(onSuccessString)
        } else {
            Failure(onFailureString)
        }
    } catch (e: Exception) {
        Failure(onFailureString)
    }
}