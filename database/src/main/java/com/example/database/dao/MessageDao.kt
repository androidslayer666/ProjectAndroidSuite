package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.CommentEntity
import com.example.database.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE projectId = :projectId")
    suspend fun getMessageByProjectId(projectId: Int): List<MessageEntity>

    @Query("SELECT * FROM message WHERE projectId = :projectId")
    fun getMessageByProjectIdFlow(projectId: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE id = :messageId")
    suspend fun getMessageByMessageId(messageId: Int): MessageEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messageList: List<MessageEntity>)

    @Query("DELETE FROM message WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)

}