package com.example.projectandroidsuite.adapter

import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.database.entities.ProjectEntity
import com.example.database.entities.SubtaskEntity
import com.example.database.entities.UserEntity
import com.example.projectandroidsuite.R
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("setStatusImage")
fun setStatusImage(view: ImageView, project: ProjectEntity) {
    when (project.status) {
        0 -> view.setImageResource(R.drawable.ic_project_status_active)
        1 -> view.setImageResource(R.drawable.ic_project_status_done)
        2 -> view.setImageResource(R.drawable.ic_project_status_paused)
    }
}

@BindingAdapter("setDocumentType")
fun setDocumentType(view: ImageView, type: String) {
    when (type) {
        ".docx" -> view.setImageResource(R.drawable.text)
        ".xlsx" -> view.setImageResource(R.drawable.excel)
    }
}


@BindingAdapter("dateShower")
fun dateShower(view: TextView, date: String?) {
    if (!date.isNullOrEmpty()) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date ?: "")
        val format = SimpleDateFormat("dd MM yyyy")
        view.text = format.format(formattedDate)
    }
}

@BindingAdapter("dateShowerDate")
fun dateShowerDate(view: TextView, date: Date?) {
    if (date != null) {
        view.text = SimpleDateFormat("dd MM yyyy").format(date)
    }
}

@BindingAdapter("showProjectTeam")
fun showProjectTeam(view: TextView, project: ProjectEntity) {
    var responsible = project.responsible?.displayName
    val participantsNumber = project.participantCount
    if (participantsNumber != null) {
        if (participantsNumber > 1) responsible += "+ ${(participantsNumber - 1).toString()}"
    }

    view.text = responsible
}

@BindingAdapter("setSubtask")
fun setSubtask(view: TextView, list: List<SubtaskEntity>) {
    view.text = list.size.toString()
}

@BindingAdapter("setTaskResponsible")
fun setTaskResponsible(view: TextView, responsibles: List<UserEntity>?) {
    var text = ""
    if (responsibles != null) {
        if (responsibles.isNotEmpty() == true) {
            text += responsibles[0].displayName
        }
        if (responsibles.size > 1) {
            text += "& others"
        }
    }
    view.text = text
}

@BindingAdapter("setHtmlText")
fun setHtmlText(textView: TextView, text: String?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
    } else {
        textView.text = Html.fromHtml(text);
    }
}

@BindingAdapter("setText")
fun setText(textView: TextView, text: String?) {
    textView.text = text
}
