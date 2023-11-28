package com.chat.joycom.ui.scene

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class JoyComScene(
    val sceneName: String,
    val icon: ImageVector,
    val body: @Composable () -> Unit
) {
    ChatList(sceneName = "ChatList", icon = Icons.Filled.AccountCircle, body = { ChatListScene() }),
    Contact(sceneName = "Contact", icon = Icons.Filled.AccountCircle, body = { ContactScene() }),
    My(sceneName = "My", icon = Icons.Filled.AccountCircle, body = { MyScene() })
}