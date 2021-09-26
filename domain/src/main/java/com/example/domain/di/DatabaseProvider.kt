package com.example.domain.di

import android.content.Context
import com.example.network.endpoints.*
import com.example.database.dao.*
import com.example.database.db.ProjectDatabase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseProvider {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ProjectDatabase {
        return ProjectDatabase.getInstance(appContext)

    }

    @Provides
    fun provideProjectDao(database: ProjectDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    fun provideFileDao(database: ProjectDatabase): FileDao {
        return database.fileDao()
    }

    @Provides
    fun provideMessageDao(database: ProjectDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideMilestoneDao(database: ProjectDatabase): MilestoneDao {
        return database.milestoneDao()
    }

    @Provides
    fun provideSubtaskDao(database: ProjectDatabase): SubtaskDao {
        return database.subtaskDao()
    }

    @Provides
    fun provideTaskDao(database: ProjectDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideUserDao(database: ProjectDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideCommentDao(database: ProjectDatabase): CommentDao {
        return database.commentDao()
    }

    @Provides
    fun provideTeamDao(database: ProjectDatabase): TeamDao {
        return database.teamDao()
    }

}