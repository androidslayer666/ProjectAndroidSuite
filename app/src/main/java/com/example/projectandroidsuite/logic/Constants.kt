package com.example.projectandroidsuite.logic

import com.example.database.entities.ProjectEntity
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import java.util.*

object Constants {

    const val FORMAT_API_DATE = "yyyy-MM-dd'T'HH:mm:ss"
    const val FORMAT_SHOW_DATE = "dd/MM/yyyy"
}


object FakeData{
    val fakeProjectEntity = ProjectEntity(
        id = 123,
        title = "TestProject",
        status = 1,
        responsible = UserEntity(
            "1234-1245-asgh",
            firstName = "Name",
            lastName = "Surname",
            email = "name.suranme@oo.com"
        ),
        description = "Just a test project",
        isPrivate = true,
        milestoneCount = 3,
        taskCount = 5,
        updated = Date(),
        created = Date()
    )

    val fakeTask = TaskEntity(
        id = 123,
        title = "TestProject",
        status = 1,
        responsible = UserEntity(
            "1234-1245-asgh",
            firstName = "Name",
            lastName = "Surname",
            email = "name.suranme@oo.com"
        ),
        description = "Just a test project",
        responsibles = mutableListOf(
            UserEntity(
                "1234-1245-asgh",
                firstName = "Name",
                lastName = "Surname",
                email = "name.suranme@oo.com"
            ),
            UserEntity(
                "1234-sd-qerfh",
                firstName = "Alfred",
                lastName = "Rodenshtern",
                email = "alfred.rodenshtern@musli.com"
            ),
        ),
        updated = Date(),
        created = Date()
    )

}