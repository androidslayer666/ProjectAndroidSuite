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
    fun projectEndPointProvider(manager: SessionManager): ProjectEndPoint {
        return buildEndPoint(ProjectEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun taskEndPointProvider(manager: SessionManager): TaskEndPoint {
        return buildEndPoint(TaskEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun milestoneEndPointProvider(manager: SessionManager): MilestoneEndPoint {
        return buildEndPoint(MilestoneEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun fileEndPointProvider(manager: SessionManager): FileEndPoint {
        return buildEndPoint(FileEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun messageEndPointProvider(manager: SessionManager): MessageEndPoint {
        return buildEndPoint(MessageEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun subtaskEndPointProvider(manager: SessionManager): SubtaskEndPoint {
        return buildEndPoint(SubtaskEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun teamEndPointProvider(manager: SessionManager): TeamEndPoint {
        return buildEndPoint(TeamEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun commentEndPointProvider(manager: SessionManager): CommentEndPoint {
        return buildEndPoint(CommentEndPoint::class.java, manager.fetchAuthToken()?:"", manager.fetchPortalAddress()?:"")
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) : SessionManager{
        return SessionManager(context)
    }

}