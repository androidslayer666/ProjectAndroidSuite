package com.example.projectandroidsuite.ui.parts

import android.os.Build
import android.text.Html
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.CommentEntity
import com.example.database.entities.UserEntity
import com.example.domain.model.Comment
import com.example.domain.model.User
import com.example.projectandroidsuite.R

@Composable
fun ListComments(
    listComments: List<Comment>? = listOf(),
    onReplyClick: (Comment) -> Unit,
    messageId: Int? = null,
    onDeleteClick: (Comment) -> Unit
) {
    var activeComment by remember { mutableStateOf("")}

    if (listComments != null) {
        Column() {
            listComments.forEach { comment ->
                CommentItem(
                    comment = comment,
                    onReplyClick = onReplyClick,
                    messageId = messageId,
                    onDeleteClick = onDeleteClick,
                    isFocused = activeComment == comment.id,
                    setActive = { comment -> activeComment = comment })
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    onReplyClick: (Comment) -> Unit,
    messageId: Int? = null,
    onDeleteClick: (Comment) -> Unit,
    isFocused: Boolean,
    setActive: (String) -> Unit
) {
    val padding = (12 * comment.commentLevel).dp
    var replyText by remember { mutableStateOf("") }
    var showReply by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        Modifier
            .padding(start = padding, top = 6.dp)
    ) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(4.dp),
            backgroundColor = MaterialTheme.colors.background
        ) {
            Column() {
                Row(modifier = Modifier.padding(4.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(start = padding)
                            .clickable {
                                setActive(comment.id)
                                showReply = false
                            }
                            .weight(5F)
                    ) {
                        if (comment.inactive != true) {
                            //todo change to real User
                            comment.createdBy?.let { CardTeamMember(user = User(id="",email= "",firstName = "",lastName = "")) }


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Text(
                                    text = Html.fromHtml(comment.text, Html.FROM_HTML_MODE_COMPACT)
                                        .toString()
                                )
                            } else {
                                Text(text = Html.fromHtml(comment.text).toString())
                            }
                        } else {
                            Text(text = "Comment has been deleted")
                        }
                    }

                    if (isFocused && comment.inactive != true) {
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
                    if (isFocused && comment.inactive != true) {
                        Image(
                            painterResource(R.drawable.ic_baseline_delete_24),
                            "",
                            modifier = Modifier
                                .clickable { showDeleteDialog = true }
                                .weight(1F),

                            )
                    }

                }
                if (showReply && isFocused && comment.inactive != true) {
                    Row {
                        TextField(
                            value = replyText,
                            onValueChange = { text -> replyText = text },
                        modifier = Modifier.weight(5F))
                        Image(
                            painterResource(R.drawable.ic_baseline_send_24),
                            "",
                            modifier = Modifier
                                .clickable {
                                    onReplyClick(
                                        Comment(
                                            id = "",
                                            text = replyText,
                                            parentId = comment.id,
                                            messageId = messageId
                                        )
                                    )
                                    showReply = false
                                }
                                .weight(1F)
                        )
                    }
                }
                if (showDeleteDialog) {
                    ConfirmationDialog(
                        text = "Do you want to delete the comment?",
                        onSubmit = { onDeleteClick(comment) },
                        { showDeleteDialog = false })
                }
            }
        }
    }
}
