package com.chat.joycom.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class JoyComScene(
    val sceneName: String,
    val icon: ImageVector,
    val body: @Composable () -> Unit,
) {
    Conversation(sceneName = "Conversation", icon = Icons.Filled.AccountCircle, body = { ConversationScene() }),
    Call(sceneName = "Call", icon = Icons.Filled.AccountCircle, body = { CallScene() }),
    Contact(sceneName = "Contact", icon = Icons.Filled.AccountCircle, body = { ContactScene() }),
    My(sceneName = "My", icon = Icons.Filled.AccountCircle, body = { MyScene() })
}