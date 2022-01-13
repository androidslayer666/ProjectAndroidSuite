package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(usersList: List<UserEntity>)

    @Query("SELECT * FROM users WHERE self is 1")
    fun getSelfProfile(): Flow<UserEntity?>

    @Query("DELETE FROM users")
    suspend fun clearLocalCache()

}
