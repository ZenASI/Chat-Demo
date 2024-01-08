package com.chat.joycom.ui.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.chat.joycom.R

enum class JoyComScene(
    @StringRes val sceneName: Int,
    val icon: ImageVector,
    val body: @Composable () -> Unit,
) {
    Group(sceneName = R.string.community, icon = Icons.Filled.AccountCircle, body = { CommunityScene() }),
    Chat(sceneName = R.string.chat, icon = Icons.Filled.AccountCircle, body = { ChatScene() }),
    Update(sceneName = R.string.update, icon = Icons.Filled.AccountCircle, body = { UpdateScene() }),
    Call(sceneName = R.string.call, icon = Icons.Filled.AccountCircle, body = { CallScene() }),
}