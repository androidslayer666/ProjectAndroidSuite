package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.CommentEntity
import com.example.database.entities.MessageEntity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.DrawSideLine

@Composable
fun ListMessages(
    listMessages: List<MessageEntity>? = listOf(),
    onReplyClick: (CommentEntity) -> Unit,
    onEditMessageClick: (MessageEntity) -> Unit,
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
                var showExpand by remember { mutableStateOf(true) }

                DrawSideLine(
                    enable = true,
                    color = MaterialTheme.colors.primary,
                    width = 20F
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column (modifier = Modifier
                                .clickable {
                                    activeMessage = message.id
                                    showButtons = !showButtons
                                    expandComments = !expandComments
                                    showExpand = !showExpand
                                    showReply = false
                                }
                                .weight(5F)){
                                Text(
                                    message.title,
                                    style = MaterialTheme.typography.h6,

                                )
                                Spacer(modifier = Modifier.size(6.dp))
                                Text(message.text)
                                Spacer(modifier = Modifier.size(6.dp))
                            }
                            if (showExpand && message.canEdit == true) {
                                Image(
                                    painterResource(R.drawable.ic_baseline_keyboard_double_arrow_down_24),
                                    "",
                                    modifier = Modifier
                                        .clickable {
                                            activeMessage = message.id
                                            showButtons = !showButtons
                                            expandComments = !expandComments
                                            showExpand = !showExpand
                                            showReply = false
                                        }
                                        .weight(1F)
                                )
                            }

                            if (showButtons && message.id == activeMessage && message.canEdit == true) {
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
                            if (showButtons && message.id == activeMessage && message.canEdit == true) {
                                Image(
                                    painterResource(R.drawable.ic_edit_button),
                                    "",
                                    modifier = Modifier
                                        .clickable { onEditMessageClick(message) }
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

                    }
                }
            }
        }
    }
}