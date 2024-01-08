package com.chat.joycom.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.PermissionDescAlert
import com.chat.joycom.ui.commom.PermissionType
import com.chat.joycom.ui.login.LoginActivity
import com.chat.joycom.ui.main.contacts.ContactsActivity
import com.chat.joycom.ui.theme.JoyComFabTheme
import com.chat.joycom.ui.theme.JoyComTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
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

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val joyComScenesList by rememberSaveable {
                mutableStateOf(JoyComScene.values().toList())
            }
            val contactPermission = rememberPermissionState(
                Manifest.permission.READ_CONTACTS
            )
            var showPermissionDesc by rememberSaveable {
                mutableStateOf(false)
            }
            val pagerState = rememberPagerState(initialPage = 1) { joyComScenesList.size }
            val context = LocalContext.current
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
                            JoyComFabTheme {
                                FloatingActionButton(
                                    onClick = {
                                        when (pagerState.currentPage) {
                                            1 -> {
                                                if (contactPermission.status.isGranted) {
                                                    ContactsActivity.start(context)
                                                } else {
                                                    if (contactPermission.status.shouldShowRationale) {
                                                        showPermissionDesc = true
                                                    } else {
                                                        contactPermission.launchPermissionRequest()
                                                    }
                                                }
                                            }

                                            else -> {}
                                        }
                                    },
                                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ) {
                                    Icon(Icons.Filled.Add, "")
                                }
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
                if (showPermissionDesc) {
                    PermissionDescAlert(
                        type = PermissionType.Contacts,
                        showState = { showPermissionDesc = it },
                        acceptCallback = {
                            contactPermission.launchPermissionRequest()
                        }
                    )
                }
            }
            LaunchedEffect(Unit) {
                viewModel.uiAction.collect { uiEvent ->
                    when (uiEvent) {
                        is UiEvent.GoLoginActEvent -> {
                            LoginActivity.start(this@MainActivity)
                            finish()
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}