package com.example.projectandroidsuite.ui.projectdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R


@Composable
fun ProjectDetailTabRow(
    state: Int,
    changeState: (Int) -> Unit,
    navigation: ProjectDetailPageNavigation
) {
    val titles = listOf("Milestones", "Messages", "Files")

    TabRow(selectedTabIndex = state) {
        titles.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier
                    .height(50.dp)
                    .background(MaterialTheme.colors.primary),
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(title)
                        if (index == 0 && state == 0) IconButton(onClick = {
                            navigation.navigateToMilestoneCreationScreen()
                        }) {
                            Image(
                                painterResource(id = R.drawable.plus_circle_outline),
                                ""
                            )
                        }

                        if (index == 1 && state == 1) IconButton(onClick = {
                            navigation.navigateToMessageCreationScreen()
                        }) {
                            Image(
                                painterResource(id = R.drawable.plus_circle_outline),
                                ""
                            )
                        }
                    }
                },
                selected = state == index,
                onClick = { changeState(index) }
            )
        }
    }
}