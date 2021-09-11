package com.example.projectandroidsuite.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.dialogs.CustomConstraintLayout
import com.example.projectandroidsuite.ui.dialogs.ProjectCreateEditDialog
import com.example.projectandroidsuite.ui.dialogs.TaskCreateEditDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabCreator(
            findViewById(R.id.layoutCustom),
            findViewById(R.id.fabAddTask),
            findViewById(R.id.fabAddProject),
            findViewById(R.id.fab),
            supportFragmentManager
        )
    }
}

fun fabCreator(
    layout: CustomConstraintLayout,
    fabAddTask: FloatingActionButton,
    fabAddProject: FloatingActionButton,
    fab: FloatingActionButton,
    supportFragmentManager: FragmentManager
){
    fab.setOnClickListener {
            layout.setProcessTouch(true)
            if (fabAddTask.visibility == View.GONE) {
                fabAddTask.visibility = View.VISIBLE
                fabAddProject.visibility = View.VISIBLE
            } else {
                fabAddTask.visibility = View.GONE
                fabAddProject.visibility = View.GONE
                layout.setProcessTouch(false)
            }
        }

    fabAddTask.setOnClickListener {
            TaskCreateEditDialog().show(supportFragmentManager, "CreateTask")
        }

    fabAddProject.setOnClickListener {
            ProjectCreateEditDialog(DialogOptions.CREATE).show(
                supportFragmentManager,
                "CreateProject"
            )
        }

    layout.setOnClickListener {
        if (fabAddTask.visibility == View.VISIBLE) {
            fabAddTask.visibility = View.GONE
            fabAddProject.visibility = View.GONE
            layout.setProcessTouch(false)
        }
    }
}