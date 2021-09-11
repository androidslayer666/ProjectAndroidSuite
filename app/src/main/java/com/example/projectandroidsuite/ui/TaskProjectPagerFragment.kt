package com.example.projectandroidsuite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.adapter.TaskProjectAdapter
import com.example.projectandroidsuite.databinding.FragmentTaskProjectPagerBinding

class TaskProjectPagerFragment: Fragment(R.layout.fragment_task_project_pager) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTaskProjectPagerBinding.inflate(inflater)
        binding.pagerTaskProject.adapter = TaskProjectAdapter(this)
        //Log.d("TaskProjectPagergment", "Launched")
        return binding.root
    }
}