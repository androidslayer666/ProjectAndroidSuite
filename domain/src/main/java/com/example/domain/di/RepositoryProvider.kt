package com.example.domain.di

import android.content.Context
import com.example.database.dao.*
import com.example.domain.repository.*
import com.example.domain.repositoryimpl.*
import com.example.network.endpoints.*
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
        commentDao: CommentDao,
        commentRepository: CommentRepository
    ): MessageRepository {
        return MessageRepositoryImpl(
            messageEndPoint,
            messageDao,
            commentEndPoint,
            commentDao,
            commentRepository
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
        taskEndPoint: TaskEndPoint,
        projectEndPoint: ProjectEndPoint
    ): TaskRepository {
        return TaskRepositoryImpl(
            taskDao, taskEndPoint, projectEndPoint
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