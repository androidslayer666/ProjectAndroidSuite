package com.example.projectandroidsuite.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectandroidsuite.ui.ProjectFragment
import com.example.projectandroidsuite.ui.TaskListFragment

private const val NUM_PAGES = 2

class TaskProjectAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int) : Fragment {
        return if (position == 0) {
            ProjectFragment()
        }else {
            TaskListFragment()
        }
    }
}