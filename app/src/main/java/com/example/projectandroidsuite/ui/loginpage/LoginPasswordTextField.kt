package com.example.projectandroidsuite.ui.loginpage

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@Composable
fun LoginPasswordTextField(
    password: String,
    onChange:  (String) -> Unit,
    passFocusTo: FocusRequester,
    isError: Boolean?,
) {

    TextField(
        value = password,
        onValueChange = { input ->
            onChange(input)
        },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        label = { Text(stringResource(R.string.password)) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { passFocusTo.requestFocus() }
        ),
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp)
            .padding(20.dp)
            .focusRequester(passFocusTo)
//            .onKeyEvent {
//                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
//                    viewModel.authenticate(onCompleteLogin = {
//                        navController.navigate(
//                            ProjectsScreens.Projects.name
//                        )
//                    })
//                    true
//                } else false
//            }
        ,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.onBackground
        ),
        isError = isError == true
    )
}