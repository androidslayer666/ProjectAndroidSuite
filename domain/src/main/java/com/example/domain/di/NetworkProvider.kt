package com.example.domain.di

import android.content.Context
import com.example.domain.AuthCredentialsProvider
import com.example.domain.repository.AuthRepositoryImpl
import com.example.network.buildEndPoint
import com.example.network.endpoints.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkApiProvider {

    @Provides
    @Singleton
    fun projectEndPointProvider(provider: AuthCredentialsProvider): ProjectEndPoint {
        return buildEndPoint(
            ProjectEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun taskEndPointProvider(provider: AuthCredentialsProvider): TaskEndPoint {
        return buildEndPoint(
            TaskEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun milestoneEndPointProvider(provider: AuthCredentialsProvider): MilestoneEndPoint {
        return buildEndPoint(
            MilestoneEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun fileEndPointProvider(provider: AuthCredentialsProvider): FileEndPoint {
        return buildEndPoint(
            FileEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun messageEndPointProvider(provider: AuthCredentialsProvider): MessageEndPoint {
        return buildEndPoint(
            MessageEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun subtaskEndPointProvider(provider: AuthCredentialsProvider): SubtaskEndPoint {
        return buildEndPoint(
            SubtaskEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun teamEndPointProvider(provider: AuthCredentialsProvider): TeamEndPoint {
        return buildEndPoint(
            TeamEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun commentEndPointProvider(provider: AuthCredentialsProvider): CommentEndPoint {
        return buildEndPoint(
            CommentEndPoint::class.java,
            provider.fetchAuthToken() ?: "",
            provider.fetchPortalAddress() ?: ""
        )
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): AuthCredentialsProvider {
        return AuthCredentialsProvider(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        authEndPoint: AuthEndPoint
    ): AuthRepositoryImpl {
        return AuthRepositoryImpl(context, authEndPoint)
    }

    @Provides
    @Singleton
    fun provideAuthEndPoint(provider: AuthCredentialsProvider): AuthEndPoint {
        val retrofit = Retrofit.Builder()
            .baseUrl(provider.fetchPortalAddress() ?: "")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AuthEndPoint::class.java)
    }

}