package com.example.projectandroidsuite.ui.loginpage

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.ui.ProjectsScreens

@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    navController: NavHostController) {

    val portalAddress = viewModel.loginPortalInput.observeAsState("")
    val emailAddress = viewModel.emailInput.observeAsState("")
    val passwordAddress = viewModel.passwordInput.observeAsState("")

    val addressIsValid = viewModel.canConnectToPortal.observeAsState(false)

    val context = LocalContext.current

    Column() {
        Text(
            text = "Welcome",
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .height(100.dp)
        )
        TextField(
            value = portalAddress.value,
            onValueChange = { input ->
                viewModel.onChangePortalAddress(input)
            })
        if (addressIsValid.value) {
            TextField(
                value = emailAddress.value,
                onValueChange = { input ->
                    viewModel.onChangeEmail(input)
                },
                label = { Text("Email") })
            TextField(
                value = passwordAddress.value,
                onValueChange = { input ->
                    viewModel.onChangePassword(input)
                },
                label = { Text("Password") })

            Button(onClick = {
                viewModel.authenticate { navController.navigate(ProjectsScreens.Projects.name) }
                Log.d("authenticated", viewModel.authenticated.value.toString())
            }) {
                Text(text = "Log In")
            }
        }
    }
}