package com.chat.joycom.ui.commom

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.chat.joycom.ui.theme.JoyComDropDownTheme

@Composable
fun DropdownColumn(
    showState: Boolean,
    onDismissRequest: () -> Unit,
    itemList: List<Int>,
    itemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
) {
    JoyComDropDownTheme {
        DropdownMenu(
            expanded = showState,
            onDismissRequest = { onDismissRequest.invoke() },
            modifier = modifier,
            offset = offset
        ) {
            itemList.forEach { stringRes ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = stringRes)) },
                    onClick = {
                        itemClick.invoke(stringRes)
                    }
                )
            }
        }
    }
}