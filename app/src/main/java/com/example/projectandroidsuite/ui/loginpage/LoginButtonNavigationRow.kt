package com.example.projectandroidsuite.ui.loginpage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class LoginPages {
    PORTAL, CREDENTIALS, CODE
}

@Composable
fun LoginButtonNavigationRow(
    selectedTab: LoginPages,
    setPage: (LoginPages) -> Unit
) {
    Row( Modifier.height(50.dp)) {
        LoginButtonNavigationRowItem(selected = selectedTab == LoginPages.PORTAL) {
            setPage(LoginPages.PORTAL)
        }
        LoginButtonNavigationRowItem(selected = selectedTab == LoginPages.CREDENTIALS) {
            setPage(LoginPages.CREDENTIALS)
        }
        LoginButtonNavigationRowItem(selected = selectedTab == LoginPages.CODE) {
            setPage(LoginPages.CODE)
        }
    }
}


@Composable
fun RowScope.LoginButtonNavigationRowItem(
    selected: Boolean,
    onClick: () -> Unit
) {
    Box (
        modifier = Modifier
            .clickable { onClick() }
            .padding(12.dp)
            .weight(1F)
    ){
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawCircle(
                color = Color.White.copy(alpha = if (selected) 1F else 0.3F)
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginButtonNavigationRowItem() {
    LoginButtonNavigationRow(
        LoginPages.PORTAL,
        {}
    )
}