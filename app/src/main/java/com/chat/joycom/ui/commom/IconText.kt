package com.chat.joycom.ui.commom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconTextV(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        icon()
        Spacer(modifier = Modifier.size(3.dp))
        text()
    }
}

@Composable
fun IconTextH(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
    textFullWeightEnable: Boolean = false,
    spaceWeightEnable: Pair<Boolean, Boolean> = Pair(false, false),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(10.dp),
    height: Dp = 80.dp,
) {
    Row(
        modifier = modifier.heightIn(min = height),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
    ) {
        icon()
        if (spaceWeightEnable.first) Spacer(modifier = Modifier.weight(1f))
        if (textFullWeightEnable) {
            Box(
                modifier = Modifier.weight(1f),
                content = { text() },
                contentAlignment = Alignment.Center
            )
        } else {
            text()
        }
        if (spaceWeightEnable.second) Spacer(modifier = Modifier.weight(1f))
        action()
    }
}

@Composable
fun IconTextH2Line(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
    textFullWeightEnable: Boolean = false,
    spaceWeightEnable: Pair<Boolean, Boolean> = Pair(false, false),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(10.dp),
    height: Dp = 80.dp,
) {
    Row(
        modifier = modifier.heightIn(min = height),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
    ) {
        icon()
        if (spaceWeightEnable.first) Spacer(modifier = Modifier.weight(1f))
        if (textFullWeightEnable) {
            Box(
                modifier = Modifier.weight(1f),
                content = {
                    Column {
                        text()
                        description()
                    }
                },
                contentAlignment = Alignment.Center
            )
        } else {
            Column {
                text()
                description()
            }
        }
        if (spaceWeightEnable.second) Spacer(modifier = Modifier.weight(1f))
        action()
    }
}
