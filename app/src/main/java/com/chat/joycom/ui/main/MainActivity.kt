package com.chat.joycom.ui.main

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.login.LoginActivity
import com.chat.joycom.ui.theme.JoyComTheme
import com.chat.joycom.ui.theme.NoRippleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContent {
            val scope = rememberCoroutineScope()
            val joyComScenesList by rememberSaveable {
                mutableStateOf(JoyComScene.values().toList())
            }
            val pagerState = rememberPagerState { joyComScenesList.size }
            JoyComTheme() {
                Scaffold(
                    bottomBar = {
                        MainBottomTabs(joyComScenesList[pagerState.currentPage],
                            onClick = { ordinal ->
                                scope.launch { pagerState.animateScrollToPage(ordinal) }
                            })
                    }
                ) { paddingValues ->
                    HorizontalPager(
                        contentPadding = paddingValues,
                        state = pagerState
                    ) { pos ->
                        joyComScenesList[pos].body.invoke()
                    }
                }
            }
        }

        initCollect()
    }

    private fun initCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it is UiEvent.GoLoginActEvent) {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBottomTabs(currentScene: JoyComScene, onClick: (Int) -> Unit) {
    val joyComScenesList = JoyComScene.values().toList()
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(80.dp)
    ) {
        joyComScenesList.forEach { item ->
            val select = currentScene.name == item.name
            val textColor = if (select) Color(0xFF9CCC65) else Color.DarkGray
            val iconColor = if (select) Color(0xFF9CCC65) else Color.DarkGray
            MainTabItem(
                modifier = Modifier.weight(1f),
                label = { Text(text = item.sceneName, color = textColor) },
                onClick = { onClick.invoke(item.ordinal) },
                icon = {
                    BadgedBox(badge = {
//                        Badge {
//                            Text(text = "0")
//                        }
                    }) {
                        Icon(imageVector = item.icon, contentDescription = "", tint = iconColor)
                    }
                }
            )
        }
    }
}

@Composable
fun MainTabItem(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    // disable click ripple
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .clickable() {
                    onClick.invoke()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            label()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainBottomTabItem_Preview_Night() {
    JoyComTheme {
        MainBottomTabs(currentScene = JoyComScene.Conversation, onClick = {})
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MainBottomTabItem_Preview_Light() {
    JoyComTheme {
        MainBottomTabs(currentScene = JoyComScene.Contact, onClick = {})
    }
}