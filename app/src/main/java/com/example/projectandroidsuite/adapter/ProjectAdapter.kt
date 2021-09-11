package com.example.projectandroidsuite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.ProjectEntity
import com.example.projectandroidsuite.databinding.ProjectItemBinding

class ProjectAdapter (
    private val fragment: OnProjectClicker
        ):
    ListAdapter<ProjectEntity, ProjectAdapter.ProjectViewHolder>(ProjectListAdapterCallBack()) {

    interface OnProjectClicker{
        fun onProjectClick(projectId: Int)
    }

    class ProjectViewHolder(private val binding: ProjectItemBinding, private val fragment: OnProjectClicker) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(project: ProjectEntity) {
            binding.project = project
            binding.root.setOnClickListener {
                fragment.onProjectClick(project.id)
            }

            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProjectItemBinding.inflate(layoutInflater, parent, false)
        return ProjectViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ProjectListAdapterCallBack : DiffUtil.ItemCallback<ProjectEntity>() {
        override fun areItemsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
            return false
            oldItem == newItem
        }
    }


}

