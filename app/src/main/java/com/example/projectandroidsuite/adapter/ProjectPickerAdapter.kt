package com.example.projectandroidsuite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.databinding.ProjectPickerItemBinding
import com.example.projectandroidsuite.databinding.TeamItemBinding

class ProjectPickerAdapter(
    private val fragment: OnProjectClicker
) :
    ListAdapter<ProjectEntity, ProjectPickerAdapter.UserViewHolder>(ProjectListAdapterCallBack()) {

    interface OnProjectClicker {
        fun onProjectClick(project: ProjectEntity)
    }

    class UserViewHolder(
        private val binding: ProjectPickerItemBinding,
        private val fragment: OnProjectClicker
    ) :

        RecyclerView.ViewHolder(binding.root) {
        fun bind(project: ProjectEntity) {
            //Log.d("UserViewHolder", project.toString())
            binding.project = project
            binding.root.setOnClickListener {
                fragment.onProjectClick(project)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ProjectPickerItemBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProjectListAdapterCallBack : DiffUtil.ItemCallback<ProjectEntity>() {
        override fun areItemsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
            //Log.d("ProjectListAdallBack", newItem.toString())
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectEntity, newItem: ProjectEntity): Boolean {
            //Log.d("ProjectListAdallBack", newItem.toString())
            return oldItem == newItem
        }
    }
}