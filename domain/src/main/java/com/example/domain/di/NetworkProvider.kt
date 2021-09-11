package com.example.domain.di

import android.content.Context
import com.example.domain.SessionManager
import com.example.network.buildEndPoint
import com.example.network.endpoints.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkApiProvider {

    @Provides
    @Singleton
    fun projectEndPointProvider(token: String): ProjectEndPoint {
        return buildEndPoint(ProjectEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun taskEndPointProvider(token: String): TaskEndPoint {
        return buildEndPoint(TaskEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun milestoneEndPointProvider(token: String): MilestoneEndPoint {
        return buildEndPoint(MilestoneEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun fileEndPointProvider(token: String): FileEndPoint {
        return buildEndPoint(FileEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun messageEndPointProvider(token: String): MessageEndPoint {
        return buildEndPoint(MessageEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun subtaskEndPointProvider(token: String): SubtaskEndPoint {
        return buildEndPoint(SubtaskEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun teamEndPointProvider(token: String): TeamEndPoint {
        return buildEndPoint(TeamEndPoint::class.java, token)
    }

    @Provides
    @Singleton
    fun commentEndPointProvider(token: String): CommentEndPoint {
        return buildEndPoint(CommentEndPoint::class.java, token)
    }


    @Provides
    @Singleton
    fun provideToken(@ApplicationContext context: Context): String {
        return SessionManager(context).fetchAuthToken()?: "No token yet"
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) : SessionManager{
        return SessionManager(context)
    }


}