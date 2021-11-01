package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.domain.model.User
import com.example.projectandroidsuite.ui.utils.coilRequestBuilder

@Composable
fun RowTeamMember(
    list: List<User>?,
    modifier : Modifier = Modifier
) {
    list?.let {
        LazyRow(modifier = modifier) {
            items(it) { user ->
                CardTeamMember(user = user, viewModel = hiltViewModel())
            }
        }
    }
}

@Composable
fun CardTeamMember(
    user: User,
    showFullName : Boolean? = false,
    viewModel: CoilViewModel = hiltViewModel()
    ) {

    Card(
        elevation = 1.dp,
        backgroundColor= MaterialTheme.colors.primary,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(6.dp, 0.dp, 6.dp, 0.dp)
    ) {
        Row(
            Modifier.clip(CircleShape)
        ) {
            user.avatarSmall?.let {
                Image(
                    painter = rememberImagePainter(
                        coilRequestBuilder(
                            user.avatarSmall!!,
                            LocalContext.current,
                            viewModel.fetchPortalAddress()?.dropLast(1)?:"",
                            viewModel.fetchToken()?:""
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = if(showFullName != true) {
                    user.displayName.substringBefore(' ')
                } else user.displayName,
                Modifier
                    .clip(CircleShape)
                    .padding(start = 6.dp, end = 6.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}
