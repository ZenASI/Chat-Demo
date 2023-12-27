package com.chat.joycom.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.login.LoginActivity
import com.chat.joycom.ui.setting.SettingActivity
import com.chat.joycom.ui.theme.JoyComTheme
import com.chat.joycom.ui.theme.TabSelectDark
import com.chat.joycom.ui.theme.TabSelectLight
import com.chat.joycom.ui.theme.TabUnSelectDark
import com.chat.joycom.ui.theme.TabUnSelectLight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalFoundationApi::class)
class MainActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, MainActivity::class.java)
            )
        }
    }

    private val viewModel by viewModels<MainActivityViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val joyComScenesList by rememberSaveable {
                mutableStateOf(JoyComScene.values().toList())
            }
            val pagerState = rememberPagerState(initialPage = 1) { joyComScenesList.size }
            JoyComTheme() {
                Scaffold(
                    topBar = {
                        Column {
                            JoyComAppBar(
                                showBack = false,
                                title = { Text(text = stringResource(id = R.string.app_name)) },
                                acton = { MainTopBarAction(pagerState) }
                            )
                            MainTableRow(
                                currentScene = joyComScenesList[pagerState.currentPage],
                                onClick = { ordinal ->
                                    scope.launch { pagerState.animateScrollToPage(ordinal) }
                                })
                        }
                    },
                    floatingActionButton = {
                        // see ref:https://issuetracker.google.com/issues/224005027
                        AnimatedVisibility(
                            visible = pagerState.currentPage != 0,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            FloatingActionButton(
                                onClick = {},
                                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ) {
                                Icon(Icons.Filled.Add, "")
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) { paddingValues ->
                    HorizontalPager(
                        contentPadding = paddingValues,
                        state = pagerState,
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
                        LoginActivity.start(this@MainActivity)
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun MainTableRow(currentScene: JoyComScene, onClick: (Int) -> Unit) {
    val joyComScenesList = JoyComScene.values().toList()
    val unSelectColor = if (isSystemInDarkTheme()) TabUnSelectDark else TabUnSelectLight
    val selectColor = if (isSystemInDarkTheme()) TabSelectDark else TabSelectLight
    TabRow(
        selectedTabIndex = currentScene.ordinal,
        divider = { },
        indicator = { tabPositions ->
            if (currentScene.ordinal < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[currentScene.ordinal]),
//                    1.dp,
                    color = selectColor,
                )
            }
        }
    ) {
        joyComScenesList.forEachIndexed { index, item ->
            Tab(
                selected = currentScene.ordinal == index,
                onClick = { onClick.invoke(index) },
                selectedContentColor = selectColor,
                unselectedContentColor = unSelectColor
            ) {
                if (index == 0) {
                    Icon(painterResource(id = R.drawable.ic_group), "")
                } else {
                    Text(
                        text = stringResource(id = item.sceneName),
                        modifier = Modifier.padding(15.dp),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainTopBarAction(pagerState: PagerState) {
    val dropDownList by remember(pagerState) {
        derivedStateOf {
            if (pagerState.currentPage == 1) {
                MainDropDown.values().toList()
            } else {
                listOf(MainDropDown.Setting)
            }
        }
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Icon(
        painterResource(id = R.drawable.ic_camera),
        "",
        modifier = Modifier.clickable {
            // TODO: open camera
        })

    AnimatedVisibility(
        visible = pagerState.currentPage != 0,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally()
    ) {
        Icon(
            painterResource(id = R.drawable.ic_search),
            "",
            modifier = Modifier.clickable {

            })
    }
    Box() {
        Icon(
            Icons.Filled.MoreVert,
            "",
            modifier = Modifier.clickable {
                isExpanded = true
            })
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },

        ) {
            dropDownList.forEach {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = it.itemName)) },
                    onClick = {
                        isExpanded = false
                        when (it) {
                            MainDropDown.Setting -> SettingActivity.start(context)

                            else -> {

                            }
                        }
                    })
            }
        }
    }
}