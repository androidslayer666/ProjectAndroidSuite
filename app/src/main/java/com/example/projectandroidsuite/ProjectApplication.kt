package com.example.projectandroidsuite

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProjectApplication: Application() {

    private var instance: ProjectApplication? = null

    fun getContext(): Context? {
        return instance
    }
}