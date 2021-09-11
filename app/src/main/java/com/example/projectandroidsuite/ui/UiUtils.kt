package com.example.projectandroidsuite.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginStart
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.database.entities.ProjectEntity
import com.example.projectandroidsuite.R
import java.text.SimpleDateFormat
import java.util.*
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.model.GlideUrl
import com.example.database.entities.SubtaskEntity
import com.example.database.entities.UserEntity
import com.example.network.dto.CommentDto
import com.example.network.dto.TaskDto
import com.example.projectandroidsuite.databinding.FragmentProjectDetailBinding
import com.example.projectandroidsuite.ui.dialogs.CustomConstraintLayout
import com.example.projectandroidsuite.ui.dialogs.DialogConfirmation
import com.example.projectandroidsuite.ui.dialogs.ProjectCreateEditDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


fun loadImageGlide(url: String?, view: ImageView?, fragment: Fragment) {
    val glideUrl = GlideUrl(
        "https://oleggonchar.onlyoffice.com$url",
        LazyHeaders.Builder()
            .addHeader(
                "Authorization",
                "Bearer jVSJtYryRhNGcMbGpTTgxT0SxBE0W8NSjC3swc4aYNeoOzKpn24nvPKMdzKEfUuiPZmK/b/xIUA53wQFpkdfAtLvfvN2lDGV6wWyClYdtt+Q6pVEll2TQ4+nd5O5jYr1nvvS9gqKjJGXQDzxld5WKoVUyhRlDrCgnQ7qA4nEMfs="
            )
            .build()
    )

    view?.let {
        Glide.with(fragment)
            .load(glideUrl)
            .override(60, 60)
            .centerCrop()
            //.placeholder(R.drawable.ic_baseline_image_24)
            .into(view)
    }
}

fun setRecyclerDecoration(recycler: RecyclerView, context: Context) {
    val dividerItemDecoration = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )
    recycler.addItemDecoration(dividerItemDecoration)
}

fun List<UserEntity>.fromListUsersToStrings(): List<String> {
    val listString = mutableListOf<String>()
    for (user in this) {
        listString.add(user.id)
    }
    return listString
}


enum class TaskDate {
    STARTDATE, ENDDATE
}

enum class DialogOptions {
    CREATE, UPDATE
}


fun List<UserEntity>.getListIds(): List<String> {
    val listIds = mutableListOf<String>()
    for (user in this) {
        listIds.add(user.id)
    }
    return listIds
}

fun List<UserEntity>.getUserById(id: String):UserEntity?{
    for (user in this) {
        if (user.id == id) return user
    }
    return null
}

fun <T> MutableLiveData<T>.forceRefresh() {
    if(this.value != null)
    this.value = this.value
}

fun setExpandableOptions(
    layout: CustomConstraintLayout,
    cardEdit: CardView,
    cardDelete: CardView,
    buttonExpand: ImageView
    ) {
    // set visibility and begin listen for outside click event
    buttonExpand.setOnClickListener {
        layout.setProcessTouch(true)
        if (cardEdit.visibility == View.GONE) {
            cardDelete.visibility = View.VISIBLE
            cardEdit.visibility = View.VISIBLE
        } else {
            cardDelete.visibility = View.GONE
            cardEdit.visibility = View.GONE
            layout.setProcessTouch(false)
        }

        // Change image for button expand
        buttonExpand.setImageResource(
            if (cardDelete.visibility == View.VISIBLE)
                R.drawable.ic_baseline_expand_less_24
            else R.drawable.ic_baseline_expand_more_24
        )
    }
    // on click outside do
    layout.setOnClickListener {
        if (cardDelete.visibility == View.VISIBLE) {
            buttonExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
            cardDelete.visibility = View.GONE
            cardEdit.visibility = View.GONE
            layout.setProcessTouch(false)
        }
    }

}