package com.example.projectandroidsuite.ui.createeditscreens.project

import com.example.domain.filters.project.ProjectStatus

sealed class CreateProjectActions(){
    class SetTitle(text:String)
    class SetDescription(text:String)
    class SetProjectStatus(status: ProjectStatus)
    class CreateProject()
    class UpdateProject()
}
