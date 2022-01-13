package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.UserEntity

@Dao
interface TeamDao {
//
//    @Query("SELECT * FROM users")
//    fun getAllUsers(): List<UserEntity>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUsers(usersList: List<UserEntity>)
//
//    @Query("DELETE FROM users")
//    suspend fun clearLocalCache()
}