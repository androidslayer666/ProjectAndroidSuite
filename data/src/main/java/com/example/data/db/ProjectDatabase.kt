package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.*
import com.example.domain.entities.*


@Database(
    entities = [ProjectEntity::class, UserEntity::class, FileEntity::class, MessageEntity::class,
        MilestoneEntity::class, TaskEntity::class, SubtaskEntity::class, CommentEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun fileDao(): FileDao
    abstract fun messageDao(): MessageDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun subtaskDao(): SubtaskDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun teamDao(): TeamDao

    companion object {
        @Volatile
        private var instance: ProjectDatabase? = null

        fun getInstance(context: Context): ProjectDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ProjectDatabase {
            return Room.databaseBuilder(context, ProjectDatabase::class.java, "projects").build()
        }
    }
}
