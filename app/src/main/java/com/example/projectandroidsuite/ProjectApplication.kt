package com.example.projectandroidsuite

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProjectApplication: Application(){
    private val context: Context? = null

    init {
        instance = this
    }

    companion object {
        private var instance: ProjectApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = ProjectApplication.applicationContext()
    }
}