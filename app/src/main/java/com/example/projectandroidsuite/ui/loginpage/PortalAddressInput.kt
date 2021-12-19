package com.example.projectandroidsuite.ui.loginpage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@Composable
fun PortalAddressInput(
    uiState: LoginScreenState,
    onChangeInput:(String) -> Unit,
    changeTab: (Boolean) -> Unit,
    onClickConnect: () -> Unit
){

    val titles =
        listOf(stringResource(R.string.cloud_portal), stringResource(R.string.local_portal))

    Row {
        TabRow(
            selectedTabIndex = if (uiState.portalIsInCloud) 0 else 1,
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = if (index == 0) uiState.portalIsInCloud else !uiState.portalIsInCloud,
                    onClick = { changeTab(if (index == 0) uiState.portalIsInCloud else !uiState.portalIsInCloud) }
                )
            }
        }
    }

    TextField(
        value = uiState.portalAddress,
        onValueChange = { input -> onChangeInput(input) },
        singleLine = true,
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp)
            .padding(bottom = 20.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.onBackground
        ),
        isError = uiState.loginInputState.isPortalValidated != true && uiState.portalAddress.isNotEmpty()
    )
    if (uiState.loginInputState.isPortalValidated != true) {
        if (uiState.portalIsInCloud) {
            Text(
                text = stringResource(R.string.input_your_portal_address_in_a_format),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(start = 50.dp, end = 50.dp)
//                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Button(onClick = { onClickConnect() }) {
                Text(stringResource(R.string.connect))
            }
        }
    }
}