package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@Composable

fun ScaffoldTopBarWrapper(
    showFilters: Boolean,
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            Row(modifier = Modifier.height(50.dp).background(MaterialTheme.colors.primary))
            {
                Text(
                    text = "Project application",
                    modifier = Modifier
                        .weight(10F)
                        .padding(12.dp)
                )
                if (showFilters) {
                    Image(
                        painterResource(R.drawable.ic_baseline_filter_alt_24),
                        "",
                        modifier = Modifier
                            .clickable { onFilterClick() }
                            .height(50.dp)
                            .padding(start = 12.dp, end = 12.dp)
                            .weight(2F)
                    )
                }
                Image(
                    painterResource(R.drawable.ic_baseline_search_24), "",
                    modifier = Modifier
                        .clickable { onSearchClick() }
                        .height(50.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .weight(2F)
                )
            }
        }
    ) {
        content()
    }
}




