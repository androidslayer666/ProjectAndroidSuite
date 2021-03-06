package com.example.projectandroidsuite.di

import android.content.Context
import com.example.data.dao.*
import com.example.data.endpoints.*
import com.example.data.repository.*
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryProvider {

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        apiService: AuthEndPoint
    ): AuthRepository {
        return AuthRepositoryImpl(context, apiService)
    }

    @Provides
    @Singleton
    fun provideCommentRepository(
        commentDao: CommentDao,
        commentEndPoint: CommentEndPoint
    ): CommentRepository {
        return CommentRepositoryImpl(commentEndPoint, commentDao)
    }

    @Provides
    @Singleton
    fun provideFileRepository(fileDao: FileDao, fileEndPoint: FileEndPoint): FileRepository {
        return FileRepositoryImpl(fileDao, fileEndPoint)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        messageEndPoint: MessageEndPoint,
        messageDao: MessageDao,
        commentEndPoint: CommentEndPoint,
        commentDao: CommentDao
    ): MessageRepository {
        return MessageRepositoryImpl(
            messageEndPoint,
            messageDao,
            commentEndPoint,
            commentDao
        )
    }

    @Provides
    @Singleton
    fun provideMilestoneRepository(
        milestoneEndPoint: MilestoneEndPoint,
        milestoneDao: MilestoneDao
    ): MilestoneRepository {
        return MilestoneRepositoryImpl(milestoneEndPoint, milestoneDao)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(
        projectDao: ProjectDao,
        projectEndPoint: ProjectEndPoint,
        teamEndPoint: TeamEndPoint,
    ): ProjectRepository {
        return ProjectRepositoryImpl(projectDao, projectEndPoint, teamEndPoint)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao,
        taskEndPoint: TaskEndPoint
    ): TaskRepository {
        return TaskRepositoryImpl(
            taskDao, taskEndPoint
        )
    }

    @Provides
    @Singleton
    fun provideTeamRepository(
        teamEndPoint: TeamEndPoint,
        userDao: UserDao
    ): TeamRepository {
        return TeamRepositoryImpl(teamEndPoint, userDao)
    }
}