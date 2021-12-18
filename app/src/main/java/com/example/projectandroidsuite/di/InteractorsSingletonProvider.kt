package com.example.projectandroidsuite.di

import com.example.domain.interactor.comment.DeleteComment
import com.example.domain.interactor.comment.GetCommentByTaskId
import com.example.domain.interactor.comment.PutCommentToMessage
import com.example.domain.interactor.comment.PutCommentToTask
import com.example.domain.interactor.file.GetFilesByProjectId
import com.example.domain.interactor.file.GetFilesByTaskId
import com.example.domain.interactor.login.*
import com.example.domain.interactor.message.*
import com.example.domain.interactor.milestone.*
import com.example.domain.interactor.project.CreateProject
import com.example.domain.interactor.project.DeleteProject
import com.example.domain.interactor.project.GetProjectById
import com.example.domain.interactor.project.UpdateProject
import com.example.domain.interactor.task.*
import com.example.domain.interactorimpl.comment.DeleteCommentImpl
import com.example.domain.interactorimpl.comment.GetCommentByTaskIdImpl
import com.example.domain.interactorimpl.comment.PutCommentToMessageImpl
import com.example.domain.interactorimpl.comment.PutCommentToTaskImpl
import com.example.domain.interactorimpl.file.GetFilesByProjectIdImpl
import com.example.domain.interactorimpl.file.GetFilesByTaskIdImpl
import com.example.domain.interactorimpl.login.*
import com.example.domain.interactorimpl.message.*
import com.example.domain.interactorimpl.milestone.*
import com.example.domain.interactorimpl.project.CreateProjectImpl
import com.example.domain.interactorimpl.project.DeleteProjectImpl
import com.example.domain.interactorimpl.project.GetProjectByIdImpl
import com.example.domain.interactorimpl.project.UpdateProjectImpl
import com.example.domain.interactorimpl.task.*
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class InteractorsSingletonProvider {

    @Provides
    @Singleton
    fun provideGetProjectById(projectRepository: ProjectRepository): GetProjectById {
        return GetProjectByIdImpl(projectRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteProject(projectRepository: ProjectRepository): DeleteProject {
        return DeleteProjectImpl(projectRepository)
    }



    @Provides
    @Singleton
    fun provideCreateProject(projectRepository: ProjectRepository): CreateProject {
        return CreateProjectImpl(projectRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProject(projectRepository: ProjectRepository): UpdateProject {
        return UpdateProjectImpl(projectRepository)
    }




    @Provides
    @Singleton
    fun provideDeleteTask(taskRepository: TaskRepository): DeleteTask {
        return DeleteTaskImpl(taskRepository)
    }

    @Provides
    @Singleton
    fun provideGetTaskById(taskRepository: TaskRepository): GetTaskById {
        return GetTaskByIdImpl(taskRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTaskStatus(taskRepository: TaskRepository): UpdateTaskStatus {
        return UpdateTaskStatusImpl(taskRepository)
    }



    @Provides
    @Singleton
    fun provideGetMilestoneById(milestoneRepository: MilestoneRepository): GetMilestoneById {
        return GetMilestoneByIdImpl(milestoneRepository)
    }

    @Provides
    @Singleton
    fun providePutMilestoneToProject(milestoneRepository: MilestoneRepository): PutMilestoneToProject {
        return PutMilestoneToProjectImpl(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateMilestone(milestoneRepository: MilestoneRepository): UpdateMilestone {
        return UpdateMilestoneImpl(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideGetMilestonesForProject(milestoneRepository: MilestoneRepository): GetMilestonesForProject {
        return GetMilestonesForProjectImpl(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMilestone(milestoneRepository: MilestoneRepository): DeleteMilestone {
        return DeleteMilestoneImpl(milestoneRepository)
    }

    @Provides
    @Singleton
    fun provideGetMessageByProjectId(messageRepository: MessageRepository): GetMessageByProjectId {
        return GetMessageByProjectIdImpl(messageRepository)
    }

    @Provides
    @Singleton
    fun provideGetMessageById(messageRepository: MessageRepository): GetMessageById {
        return GetMessageByIdImpl(messageRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMessage(messageRepository: MessageRepository): DeleteMessage {
        return DeleteMessageImpl(messageRepository)
    }

    @Provides
    @Singleton
    fun provideCreateMessage(messageRepository: MessageRepository): CreateMessage {
        return CreateMessageImpl(messageRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateMessage(messageRepository: MessageRepository): UpdateMessage {
        return UpdateMessageImpl(messageRepository)
    }

    @Provides
    @Singleton
    fun provideGetCommentByTaskId(commentRepository: CommentRepository): GetCommentByTaskId {
        return GetCommentByTaskIdImpl(commentRepository)
    }

    @Provides
    @Singleton
    fun providePutCommentToTask(commentRepository: CommentRepository): PutCommentToTask {
        return PutCommentToTaskImpl(commentRepository)
    }

    @Provides
    @Singleton
    fun providePutCommentToMessage(
        commentRepository: CommentRepository,
        messageRepository: MessageRepository
    ): PutCommentToMessage {
        return PutCommentToMessageImpl(commentRepository, messageRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteComment(commentRepository: CommentRepository): DeleteComment {
        return DeleteCommentImpl(commentRepository)
    }

    @Provides
    @Singleton
    fun provideCreateTask(taskRepository: TaskRepository): CreateTask {
        return CreateTaskImpl(taskRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTask(taskRepository: TaskRepository): UpdateTask {
        return UpdateTaskImpl(taskRepository)
    }

    @Provides
    @Singleton
    fun provideCreateSubtask(taskRepository: TaskRepository): CreateSubtask {
        return CreateSubtaskImpl(taskRepository)
    }



    @Provides
    @Singleton
    fun provideGetFilesByProjectId(
        fileRepository: FileRepository
    ): GetFilesByProjectId {
        return GetFilesByProjectIdImpl(fileRepository)
    }

    @Provides
    @Singleton
    fun provideGetFilesByTaskId(fileRepository: FileRepository): GetFilesByTaskId {
        return GetFilesByTaskIdImpl(fileRepository)
    }


    @Provides
    @Singleton
    fun provideLogout(authRepository: AuthRepository): Logout {
        return LogoutImpl(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetSelfProfile(teamRepository: TeamRepository): GetSelfProfile {
        return GetSelfProfileImpl(teamRepository)
    }

    @Provides
    @Singleton
    fun provideCheckIfAuthenticated(authRepository: AuthRepository): CheckIfAuthenticated {
        return CheckIfAuthenticatedImpl(authRepository)
    }

    @Provides
    @Singleton
    fun provideRememberPortalAddress(authRepository: AuthRepository): RememberPortalAddress {
        return RememberPortalAddressImpl(authRepository)
    }

    @Provides
    @Singleton
    fun provideCheckPortalPossibilities(authRepository: AuthRepository): CheckPortalPossibilities {
        return CheckPortalPossibilitiesImpl(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogin(authRepository: AuthRepository): Login {
        return LoginImpl(authRepository)
    }


}