package com.example.domain

import android.util.Log

import com.example.network.dto.*
import com.example.network.endpoints.*
import com.example.database.dao.*
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val fileDao: FileDao,
    private val messageDao: MessageDao,
    private val milestoneDao: MilestoneDao,
    private val subtaskDao: SubtaskDao,
    private val taskDao: TaskDao,
    private val userDao: UserDao,
    private val projectEndPoint: ProjectEndPoint,
    private val taskEndPoint: TaskEndPoint,
    private val milestoneEndPoint: MilestoneEndPoint,
    private val userEndPoint: TeamEndPoint,
    private val commentEndPoint: CommentEndPoint,
    private val messageEndPoint: MessageEndPoint,
    private val fileEndPoint: FileEndPoint,
) {

    suspend fun populateDB() {
        try {
            //val projects = projectEndPoint.getAllProjects().listProjectDtos
            val project = projectEndPoint.getAllProjects()
            Log.d("projectEndPoint",project.toString())
//                .enqueue(object: Callback<ProjectsTransporter>{
//                override fun onResponse(
//                    call: Call<ProjectsTransporter>,
//                    response: Response<ProjectsTransporter>
//                ) {
//                    Log.d("projectEndPoint",response.toString())
//                }
//
//                override fun onFailure(call: Call<ProjectsTransporter>, t: Throwable) {
//                    Log.d("projectEndPoint",t.toString())
//                }
//
//            })
//            Log.d("ProjectRepository", projects.toString())
//            Log.d("ProjectRepository", "User task " + taskEndPoint.getUserTask().toString())
//            if (projects != null) {
//                if (projects.isNotEmpty()) {
//                    Log.d("ProjectRepository", projects[0].id.toString())
//
//                    Log.d("ProjectRepository",
//                        projectEndPoint.getProjectById(648126).toString()
//                    )
//                    Log.d(
//                        "ProjectRepository",
//                        "Files" +
//                                fileEndPoint.getProjectFiles(648126).toString()
//                    )
//                    Log.d(
//                        "ProjectRepository",
//                        "Tasks" + taskEndPoint.getUserTask()
//
//                    )
//                    Log.d(
//                        "ProjectRepository",
//                        "Milestones" + milestoneEndPoint.getLateMilestones().listMilestones?.size +
//                                milestoneEndPoint.getLateMilestones().listMilestones?.toString()
//                    )
//                }



        } catch (e: HttpException) {
            Log.d("ProjectRepository", e.message().toString())
        }
    }

    val testProject = ProjectPost(
        title = "TheBestProject",
        description = "sdvsd",
        responsibleId = "d71284d7-5e5b-4b3e-afd4-232c0185fa6e",
        tags = "",
        private = true,
        taskDtos = listOf(),
        milestoneDtos = listOf()
    )
    val testMilestone = MilestonePost(
        title = "TheBestMilestone",
        description = "sdvsd",
        responsible = "d71284d7-5e5b-4b3e-afd4-232c0185fa6e",
        deadline = "2008-04-10T06-30-00.000Z",
        isKey = true,
        isNotify = true,
        notifyResponsible = true
    )
}