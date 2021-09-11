package com.example.projectandroidsuite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.*
import com.example.projectandroidsuite.databinding.FileItemBinding
import com.example.projectandroidsuite.databinding.MilestoneItemBinding
import com.example.projectandroidsuite.databinding.ProjectItemBinding
import com.example.projectandroidsuite.databinding.TaskItemBinding

class UniversalAdapter(
    private val fragment: OnItemClicker
) :
    ListAdapter<UniversalEntity, RecyclerView.ViewHolder>(ProjectListAdapterCallBack()) {

    interface OnItemClicker {
        fun onItemClick(itemId: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MilestoneEntity -> 0
            is TaskEntity -> 1
            is FileEntity -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = MilestoneItemBinding.inflate(layoutInflater, parent, false)
                return MilestoneViewHolder(binding, fragment)
            }
            1 -> {
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
                TaskViewHolder(binding, fragment)
            }
            else -> {
                val binding = FileItemBinding.inflate(layoutInflater, parent, false)
                FileViewHolder(binding, fragment)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TaskViewHolder -> {
                holder.bind(item)
            }
            is MilestoneViewHolder -> {
                holder.bind(item)
            }
            is FileViewHolder -> {
                holder.bind(item)
            }

        }
    }

    //implement diffutil with multitype
    class ProjectListAdapterCallBack : DiffUtil.ItemCallback<UniversalEntity>() {
        override fun areItemsTheSame(oldItem: UniversalEntity, newItem: UniversalEntity): Boolean {
            //return false
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UniversalEntity,
            newItem: UniversalEntity
        ): Boolean {
            //return false
            return oldItem == newItem
        }
    }

    class TaskViewHolder(
        private val binding: TaskItemBinding,
        private val fragment: OnItemClicker
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: UniversalEntity) {
            binding.task = task as TaskEntity?
            binding.root.setOnClickListener {
                fragment.onItemClick(task.id)
            }
            binding.executePendingBindings()
        }
    }

    class MilestoneViewHolder(
        private val binding: MilestoneItemBinding,
        private val fragment: OnItemClicker
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(milestone: UniversalEntity) {
            binding.milestone = milestone as MilestoneEntity?
            binding.root.setOnClickListener {
                fragment.onItemClick(milestone.id)
            }
            binding.executePendingBindings()
        }
    }

    class FileViewHolder(
        private val binding: FileItemBinding,
        private val fragment: OnItemClicker
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: UniversalEntity) {
            binding.file = file as FileEntity?
            binding.root.setOnClickListener {
                fragment.onItemClick(file.id)
            }
            binding.executePendingBindings()
        }
    }
}

