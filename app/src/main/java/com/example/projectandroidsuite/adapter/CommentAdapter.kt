package com.example.projectandroidsuite.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.database.entities.CommentEntity
import com.example.projectandroidsuite.databinding.CommentItemRecyclerBinding

class CommentAdapter(
    private val fragment: OnCommentReplyClicker
) :
    ListAdapter<CommentEntity, CommentAdapter.ProjectViewHolder>(CommentAdapterCallBack()) {

    interface OnCommentReplyClicker {
        fun onCommentReplyClick(comment: CommentEntity)
    }

    class ProjectViewHolder(
        private val binding: CommentItemRecyclerBinding,
        private val fragment: OnCommentReplyClicker
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentEntity, previousComment: CommentEntity? = null) {
            if (comment.parentId == previousComment?.id) {
                binding.ifPadding = true
            }
            binding.comment = comment
            //Log.d("CommentAdapter", currentComment.toString())
            binding.root.setOnClickListener {
                //fragment.onCommentReplyClick(comment.id)
            }

            binding.buttonReply.setOnClickListener {
                if (binding.commentEditText.visibility == View.VISIBLE) {
                    fragment.onCommentReplyClick(comment)
                    binding.commentEditText.visibility = View.GONE
                } else {
                    binding.commentEditText.visibility = View.VISIBLE
                }
            }

            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CommentItemRecyclerBinding.inflate(layoutInflater, parent, false)
        return ProjectViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        if (position == 0)
            holder.bind(getItem(position))
        else holder.bind(getItem(position), getItem(position - 1))
    }

    class CommentAdapterCallBack : DiffUtil.ItemCallback<CommentEntity>() {
        override fun areItemsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
            return false
            oldItem == newItem
        }
    }
}

