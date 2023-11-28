package com.chat.joycom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.chat.joycom.ui.scene.JoyComScene
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scope = rememberCoroutineScope()
            val joyComScenesList = JoyComScene.values().toList()
            val pagerState = rememberPagerState(initialPage = 0) { joyComScenesList.size }
            var currentScene by rememberSaveable {
                // init scene
                mutableStateOf(JoyComScene.ChatList)
            }
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .distinctUntilChanged()
                    .collect { page ->
                        currentScene = when (page) {
                            0 -> JoyComScene.ChatList
                            1 -> JoyComScene.Contact
                            2 -> JoyComScene.My
                            else -> JoyComScene.ChatList
                        }
                    }
            }
            JoyComTheme() {
                Scaffold(
                    bottomBar = {
                        MainBottomTabs(currentScene,
                            onClick = { scene ->
                                scope.launch { pagerState.animateScrollToPage(scene.ordinal) }
                            })
                    }
                ) { paddingValues ->
                    HorizontalPager(
                        contentPadding = paddingValues,
                        state = pagerState
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            currentScene.body.invoke()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBottomTabs(currentScene: JoyComScene, onClick: (JoyComScene) -> Unit) {
    val joyComScenesList = JoyComScene.values().toList()
    NavigationBar {
        joyComScenesList.forEach { item ->
            NavigationBarItem(
                label = { Text(text = item.sceneName) },
                selected = currentScene.name == item.name,
                onClick = { onClick.invoke(item) },
                icon = {
                    BadgedBox(badge = {
                        Badge {
                            Text(text = "888")
                        }
                    }) {
                        Icon(imageVector = item.icon, contentDescription = "")
                    }
                })
        }
    }
}