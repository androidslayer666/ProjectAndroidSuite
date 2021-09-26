package com.example.projectandroidsuite.ui.parts

import android.os.Build
import android.text.Html
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.CommentEntity
import com.example.projectandroidsuite.R

@Composable
fun ListComments(
    listComments: List<CommentEntity>? = listOf(),
    onReplyClick: (CommentEntity) -> Unit,
    onDeleteClick: (CommentEntity) -> Unit
) {
    if (listComments != null) {
        LazyColumn {
            items(listComments) { comment ->
                CommentItem(comment, onReplyClick, null, onDeleteClick)
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: CommentEntity,
    onReplyClick: (CommentEntity) -> Unit,
    messageId: Int? = null,
    onDeleteClick: (CommentEntity) -> Unit,
) {
    val padding = (12 * comment.commentLevel).dp
    var replyText by remember { mutableStateOf("") }
    var showReply by remember { mutableStateOf(false) }
    var showReplyButton by remember { mutableStateOf(false) }
    var showDeleteButton by remember { mutableStateOf(false) }

    Row {


        Column(
            modifier = Modifier
                .padding(start = padding)
                .clickable {
                    showReplyButton = !showReplyButton
                    showDeleteButton = !showDeleteButton
                }
        ) {
            comment.createdBy?.let { TeamMemberCard(user = it) }
            Row() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Text(text = Html.fromHtml(comment.text, Html.FROM_HTML_MODE_COMPACT).toString())
                } else {
                    Text(text = Html.fromHtml(comment.text).toString())
                }
            }


            if (showReply) {
                Row {
                    TextField(value = replyText, onValueChange = { text -> replyText = text })
                    Image(
                        painterResource(R.drawable.ic_baseline_send_24),
                        "",
                        modifier = Modifier
                            .clickable {
                                onReplyClick(
                                    CommentEntity(
                                        id = "",
                                        text = replyText,
                                        createdBy = comment.createdBy,
                                        parentId = comment.id,
                                        messageId = messageId
                                    )
                                )
                                showReply = false
                            }
                    )
                }
            }
        }
        if (showReplyButton) {
            Image(
                painterResource(R.drawable.ic_baseline_comment_24),
                "",
                modifier = Modifier
                    .clickable { showReply = true
                        showReplyButton = false
                        showDeleteButton = false
                    })
        }
        if (showDeleteButton) {
            Image(
                Icons.Default.Delete,
                "",
                modifier = Modifier
                    .clickable { onDeleteClick(comment) })
        }
    }
}