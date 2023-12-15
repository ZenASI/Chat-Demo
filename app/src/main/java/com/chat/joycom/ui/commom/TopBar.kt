package com.chat.joycom.ui.commom

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoyComAppBar(
    modifier: Modifier = Modifier,
    showBack: Boolean = false,
    title: @Composable () -> Unit = {},
    acton: @Composable RowScope.() -> Unit = {},
) {
    val activity = LocalContext.current as ComponentActivity
    TopAppBar(
        navigationIcon = {
            if (showBack) Icon(
                Icons.Filled.ArrowBack,
                "",
                modifier = Modifier.clickable { activity.finish() })
        },
        title = title,
        modifier = modifier,
        actions = acton
    )
}