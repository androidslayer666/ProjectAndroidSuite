package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.database.entities.CommentEntity
import com.example.database.entities.MessageEntity

@Composable
fun ListMessages(
    listMessages: List<MessageEntity>? = listOf(),
    onReplyClick: (CommentEntity) -> Unit
) {



    listMessages?.let {
        LazyColumn() {
            items(listMessages) { message ->
                var expandComments by remember{ mutableStateOf(false)}
                Text(message.title, style = MaterialTheme.typography.h5, modifier =Modifier.clickable{expandComments = !expandComments})
                if(expandComments)
                message.listMessages?.forEach { comment ->
                    CommentItem(
                        comment = comment,
                        onReplyClick = {comment -> onReplyClick(comment)},
                        messageId = message.id,
                        onDeleteClick = {})
                }
            }
        }
    }
}