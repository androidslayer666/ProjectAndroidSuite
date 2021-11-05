package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import android.view.KeyEvent
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
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R


@Composable
fun LoginEmailTextField (
    emailAddressInput: String,
    onChange: (String) -> Unit,
    isError: Boolean?,
    focusRequester: FocusRequester,
    passFocusTo: FocusRequester
){

    TextField(
        value = emailAddressInput,
        onValueChange = { input ->
            onChange(input)
        },
        label = { Text(stringResource(R.string.email)) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            textColor = MaterialTheme.colors.onBackground
        ),
        isError = isError == true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusRequester.requestFocus() }
        ),
        modifier = Modifier
            .defaultMinSize(minWidth = 300.dp)
            .padding(20.dp)
            .focusRequester(passFocusTo)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusRequester.requestFocus()
                }
                false
            })
}