package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments")
    fun getAllComments(): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE taskId = :taskId")
    suspend fun getCommentsByTaskId(taskId: Int): List<CommentEntity>

    @Query("SELECT * FROM comments WHERE taskId = :taskId")
    fun getCommentsByTaskIdFlow(taskId: Int): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(commentList: List<CommentEntity>)

    @Query("SELECT * FROM comments WHERE messageId = :messageId")
    suspend fun getCommentsByMessageId(messageId: Int): List<CommentEntity>

    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: String)

    @Query("DELETE FROM comments")
    suspend fun clearLocalCache()


}