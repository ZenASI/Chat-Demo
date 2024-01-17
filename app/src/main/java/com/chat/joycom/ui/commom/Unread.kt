package com.chat.joycom.ui.commom

import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.chat.joycom.ui.theme.JoyComBadgeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeView(count: Int = 0) {
    val formatCount = if (count == 0) {
        ""
    } else if (count >= 100) {
        "99+"
    } else {
        count.toString()
    }
    JoyComBadgeTheme {
        Badge() { Text(text = formatCount, fontSize = 16.sp) }
    }
}