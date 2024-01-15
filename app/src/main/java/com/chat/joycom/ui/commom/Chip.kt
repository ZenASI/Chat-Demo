package com.chat.joycom.ui.commom

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chat.joycom.ui.theme.JoyComChipSelectTheme
import com.chat.joycom.ui.theme.JoyComChipTheme

@Composable
fun BasicChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit = {},
    leadingIcon: @Composable (() -> Unit) = {},
) {
    JoyComChipTheme {
        AssistChip(
            onClick = { onClick.invoke() },
            label = { label() },
            leadingIcon = {
                leadingIcon()
            },
            shape = RoundedCornerShape(40.dp),
            border = AssistChipDefaults.assistChipBorder(borderColor = Color.Transparent),
            colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surface)
        )
    }
}

@Composable
fun SelectChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit = {},
    leadingIcon: @Composable (() -> Unit) = {},
){
    JoyComChipSelectTheme {
        AssistChip(
            onClick = { onClick.invoke() },
            label = { label() },
            leadingIcon = {
                leadingIcon()
            },
            shape = RoundedCornerShape(40.dp),
            border = AssistChipDefaults.assistChipBorder(borderColor = Color.Transparent),
            colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surface)
        )
    }
}