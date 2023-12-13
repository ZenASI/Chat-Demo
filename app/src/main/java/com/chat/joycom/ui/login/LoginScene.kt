package com.chat.joycom.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class LoginScene(
    val sceneName: String,
    val icon: ImageVector,
    val body: @Composable () -> Unit,
) {
    Login(sceneName = "Login", icon = Icons.Filled.AccountBox, body = { LoginView() }),
    Register(sceneName = "Register", icon = Icons.Filled.AccountBox, body = { RegisterView() })
}