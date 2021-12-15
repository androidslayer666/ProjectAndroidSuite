package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE projectId = :projectId")
    suspend fun getMessageByProjectId(projectId: Int): List<MessageEntity>

    @Query("SELECT * FROM message WHERE projectId = :projectId")
    fun getMessageByProjectIdFlow(projectId: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE id = :messageId")
    fun getMessageByMessageId(messageId: Int): Flow<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messageList: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM message WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)

}