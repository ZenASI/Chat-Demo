package com.chat.joycom.ui.main.contacts.add.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.theme.JoyComFabTheme
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewGroupActivity : BaseActivity() {

    private val viewModel by viewModels<NewGroupViewModel>()

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, NewGroupActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            JoyComTheme {
                Scaffold(
                    topBar = {
                        JoyComAppBar(
                            title = {
                                Column {
                                    Text(text = stringResource(id = R.string.new_group))
                                    Text(
                                        text = stringResource(id = R.string.add_member),
                                        fontSize = 14.sp
                                    )
                                }
                            },
                            acton = {
                                TopBarIcon(R.drawable.ic_search, onClick = {})
                            }
                        )
                    },
                    floatingActionButton = {
                        JoyComFabTheme {
                            FloatingActionButton(onClick = {
                                viewModel.createGroupCheck()
                            }) {
                                Icon(Icons.Filled.ArrowForward, "")
                            }
                        }
                    },
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.size(15.dp))
                        NewGroupSelectContactRow()
                        NewGroupColumnList()
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.uiAction.collect { uiEvent ->
                    when (uiEvent) {
                        is UiEvent.ToastEvent -> {
                            scope.launch {
                                snackBarHostState.showSnackbar(message = getString(R.string.need_select_at_least_one_member))
                            }
                        }

                        is UiEvent.GoAddGroupEvent -> {
                            AddGroupActivity.start(this@NewGroupActivity, uiEvent.list)
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}