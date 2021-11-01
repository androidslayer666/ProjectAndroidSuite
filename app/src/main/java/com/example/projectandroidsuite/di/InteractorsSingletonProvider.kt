package com.example.projectandroidsuite.di

import com.example.domain.interactor.CreateSubtask
import com.example.domain.interactor.GetFilesByTaskId
import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.interactor.comment.GetCommentByTaskId
import com.example.domain.interactor.comment.PutCommentToMessage
import com.example.domain.interactor.comment.PutCommentToTask
import com.example.domain.interactor.files.GetAllFiles
import com.example.domain.interactor.files.GetFilesByProjectId
import com.example.domain.interactor.login.*
import com.example.domain.interactor.message.CreateMessage
import com.example.domain.interactor.message.DeleteMessage
import com.example.domain.interactor.message.GetMessageByProjectId
import com.example.domain.interactor.message.UpdateMessage
import com.example.domain.interactor.milestone.*
import com.example.domain.interactor.project.*
import com.example.domain.interactor.task.*
import com.example.domain.interactor.user.GetAllUsers
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton





@InstallIn(SingletonComponent::class)
@Module
class InteractorsSingletonProvider {

    @Provides
    @Singleton
    fun provideGetProjectById(projectRepository: ProjectRepository): GetProjectById {
        return GetProjectById(projectRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteProject(projectRepository: ProjectRepository): DeleteProject {
        return DeleteProject(projectRepository)
    }



    @Provides
    @Singleton
    fun provideCreateProject(projectRepository: ProjectRepository): CreateProject {
        return CreateProject(projectRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProject(projectRepository: ProjectRepository): UpdateProject {
        return UpdateProject(projectRepository)
    }




    @Provides
    @Singleton
    fun provideDeleteTask(taskRepository: TaskRepository): DeleteTask {
        return DeleteTask(taskRepository)
    }

    @Provides
    @Singleton
    fun provideGetTaskById(taskRepository: TaskRepository): GetTaskById {
        return GetTaskById(taskRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTaskStatus(taskRepository: TaskRepository): UpdateTaskStatus {
        return UpdateTaskStatus(taskRepository)
    }



    @Provides
    @Singleton
    fun provideGetMilestoneById(milestoneRepository: MilestoneRepository): GetMilestoneById {
        return GetMilestoneById(milestoneRepository)
    }

    @Provides
    @Singleton
    fun providePutMilestoneToProject(milestoneRepository: MilestoneRepository): PutMilestoneToProject {
        return PutMilestoneToProject(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateMilestone(milestoneRepository: MilestoneRepository): UpdateMilestone {
        return UpdateMilestone(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideGetMilestonesForProject(milestoneRepository: MilestoneRepository): GetMilestonesForProject {
        return GetMilestonesForProject(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMilestone(milestoneRepository: MilestoneRepository): DeleteMilestone {
        return DeleteMilestone(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideGetMessageByProjectId(messageRepository: MessageRepository): GetMessageByProjectId {
        return GetMessageByProjectId(messageRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMessage(messageRepository: MessageRepository): DeleteMessage {
        return DeleteMessage(messageRepository)
    }

    @Provides
    @Singleton
    fun provideCreateMessage(messageRepository: MessageRepository): CreateMessage {
        return CreateMessage(messageRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateMessage(messageRepository: MessageRepository): UpdateMessage {
        return UpdateMessage(messageRepository)
    }

    @Provides
    @Singleton
    fun provideGetCommentByTaskId(commentRepository: CommentRepository): GetCommentByTaskId {
        return GetCommentByTaskId(commentRepository)
    }

    @Provides
    @Singleton
    fun providePutCommentToTask(commentRepository: CommentRepository): PutCommentToTask {
        return PutCommentToTask(commentRepository)
    }

    @Provides
    @Singleton
    fun providePutCommentToMessage(
        commentRepository: CommentRepository,
        messageRepository: MessageRepository
    ): PutCommentToMessage {
        return PutCommentToMessage(commentRepository, messageRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteComment(commentRepository: CommentRepository): DeleteComment {
        return DeleteComment(commentRepository)
    }

    @Provides
    @Singleton
    fun provideCreateTask(taskRepository: TaskRepository): CreateTask {
        return CreateTask(taskRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTask(taskRepository: TaskRepository): UpdateTask {
        return UpdateTask(taskRepository)
    }

    @Provides
    @Singleton
    fun provideCreateSubtask(taskRepository: TaskRepository): CreateSubtask {
        return CreateSubtask(taskRepository)
    }



    @Provides
    @Singleton
    fun provideGetFilesByProjectId(
        fileRepository: FileRepository
    ): GetFilesByProjectId {
        return GetFilesByProjectId(fileRepository)
    }

    @Provides
    @Singleton
    fun provideGetFilesByTaskId(fileRepository: FileRepository): GetFilesByTaskId {
        return GetFilesByTaskId(fileRepository)
    }


    @Provides
    @Singleton
    fun provideLogout(authRepository: AuthRepository): Logout {
        return Logout(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetSelfProfile(teamRepository: TeamRepository): GetSelfProfile {
        return GetSelfProfile(teamRepository)
    }

    @Provides
    @Singleton
    fun provideCheckIfAuthenticated(authRepository: AuthRepository): CheckIfAuthenticated {
        return CheckIfAuthenticated(authRepository)
    }

    @Provides
    @Singleton
    fun provideRememberPortalAddress(authRepository: AuthRepository): RememberPortalAddress {
        return RememberPortalAddress(authRepository)
    }

    @Provides
    @Singleton
    fun provideCheckPortalPossibilities(authRepository: AuthRepository): CheckPortalPossibilities {
        return CheckPortalPossibilities(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogin(authRepository: AuthRepository): Login {
        return Login(authRepository)
    }


}