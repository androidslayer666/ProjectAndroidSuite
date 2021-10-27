package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.File
import com.example.projectandroidsuite.R

@Composable
fun ListFiles(listFiles: List<File>) {
    LazyColumn {
        items(listFiles) { file ->
            Row(Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(1F).padding(start= 12.dp)) {
                    when (file.fileExst) {
                        ".docx" -> Image(painterResource(R.drawable.file_document_outline), "")
                        ".xlsx" -> Image(painterResource(R.drawable.file_table_outline), "")
                        ".pptx" -> Image(painterResource(R.drawable.file_powerpoint_outline), "")
                        else -> {
                        }
                    }
                }

                file.title?.let { Text(text = it, Modifier.weight(5F)) }
            }
            Row {
                Row(Modifier.weight(1F).padding(start= 12.dp)) {
                }
                Divider(
                    color = MaterialTheme.colors.primary,
                    thickness = 1.dp,
                    modifier = Modifier.width(100.dp).weight(5F)
                )
            }
        }
    }
}