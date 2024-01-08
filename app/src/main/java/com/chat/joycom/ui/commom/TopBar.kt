package com.chat.joycom.ui.commom

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.chat.joycom.ui.theme.JoyComTopBarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoyComAppBar(
    modifier: Modifier = Modifier,
    showBack: Boolean = false,
    title: @Composable () -> Unit = {},
    acton: @Composable RowScope.() -> Unit = {},
) {
    val activity = LocalContext.current as ComponentActivity
    JoyComTopBarTheme {
        TopAppBar(
            navigationIcon = {
                if (showBack) Icon(
                    Icons.Filled.ArrowBack,
                    "",
                    modifier = Modifier.clickable { activity.finish() })
            },
            title = title,
            modifier = modifier.background(Color.Red),
            actions = acton,
        )
    }
}