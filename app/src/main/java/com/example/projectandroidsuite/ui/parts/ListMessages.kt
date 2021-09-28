package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.database.entities.CommentEntity
import com.example.database.entities.MessageEntity
import com.example.projectandroidsuite.R

@Composable
fun ListMessages(
    listMessages: List<MessageEntity>? = listOf(),
    onReplyClick: (CommentEntity) -> Unit,
    onDeleteMessageClick: (MessageEntity) -> Unit,
    onDeleteCommentClick: (CommentEntity) -> Unit
) {
    var activeMessage by remember { mutableStateOf(0) }

    listMessages?.let {
        LazyColumn() {
            items(listMessages) { message ->
                var expandComments by remember { mutableStateOf(false) }
                var replyText by remember { mutableStateOf("") }
                var showButtons by remember { mutableStateOf(false) }
                var showReply by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            message.title,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .clickable {
                                    activeMessage = message.id
                                    showButtons = !showButtons
                                    expandComments = !expandComments
                                    showReply = false
                                }
                                .weight(5F)
                        )
                        if (showButtons && message.id == activeMessage) {
                            Image(
                                painterResource(R.drawable.ic_baseline_comment_24),
                                "",
                                modifier = Modifier
                                    .clickable {
                                        showReply = true
                                    }
                                    .weight(1F)
                            )
                        }
                        if (showButtons && message.id == activeMessage) {
                            Image(
                                painterResource(R.drawable.ic_baseline_delete_24),
                                "",
                                modifier = Modifier
                                    .clickable { showDeleteDialog = true }
                                    .weight(1F),

                                )
                        }

                    }
                    if (showReply && message.id == activeMessage) {
                        Row {
                            TextField(
                                value = replyText,
                                onValueChange = { text -> replyText = text },
                                modifier = Modifier.weight(5F)
                            )
                            Image(
                                painterResource(R.drawable.ic_baseline_send_24),
                                "",
                                modifier = Modifier
                                    .clickable {
                                        onReplyClick(
                                            CommentEntity(
                                                id = "",
                                                text = replyText,
                                                messageId = message.id
                                            )
                                        )
                                        showReply = false
                                    }
                                    .weight(1F)
                            )
                        }
                    }
                    if (expandComments)
                        ListComments(
                            listComments = message.listMessages,
                            onReplyClick = { comment -> onReplyClick(comment) },
                            messageId = message.id,
                            onDeleteClick = { comment -> onDeleteCommentClick(comment) }
                        )
                    if (showDeleteDialog) {
                        ConfirmationDialog(
                            text = "Do you want to delete the message?",
                            onSubmit = { onDeleteMessageClick(message) },
                            { showDeleteDialog = false })
                    }
                }
            }
        }
    }
}