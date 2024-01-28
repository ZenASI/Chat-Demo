package com.chat.joycom.ui.commom

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chat.joycom.ui.theme.OnTabSelectDark
import com.chat.joycom.ui.theme.OnTabSelectLight
import com.chat.joycom.ui.theme.OnTabUnSelectDark
import com.chat.joycom.ui.theme.OnTabUnSelectLight
import com.chat.joycom.ui.theme.TabRowDark
import com.chat.joycom.ui.theme.TabRowLight

@Composable
fun JoyComTabRow(selectIndex: Int, tabs: @Composable () -> Unit) {
    val containerColor = if (isSystemInDarkTheme()) TabRowDark else TabRowLight
    val selectColor = if (isSystemInDarkTheme()) OnTabSelectDark else OnTabSelectLight
    val unSelectColor = if (isSystemInDarkTheme()) OnTabUnSelectDark else OnTabUnSelectLight

    TabRow(
        selectedTabIndex = selectIndex,
        divider = { },
        containerColor = containerColor,
        indicator = { tabPositions ->
            if (selectIndex < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectIndex]),
                    color = selectColor
                )
            }
        },
        tabs = tabs
    )
}

@Composable
fun JoyComTab(
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val selectColor = if (isSystemInDarkTheme()) OnTabSelectDark else OnTabSelectLight
    val unSelectColor = if (isSystemInDarkTheme()) OnTabUnSelectDark else OnTabUnSelectLight
    Tab(
        selected = selected,
        onClick = { onClick.invoke() },
        content = content,
        selectedContentColor = selectColor,
        unselectedContentColor = unSelectColor
    )
}