package com.chat.joycom.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.login.LoginActivity
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity() {
    private val viewModel by viewModels<SettingViewModel>()

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, SettingActivity::class.java)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme() {
                Surface {
                    Scaffold(
                        topBar = {
                            JoyComAppBar(
                                showBack = true,
                                title = { Text(text = stringResource(id = R.string.setting)) },
                                acton = { Icon(painterResource(id = R.drawable.ic_search), "") }
                            )
                        },
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .verticalScroll(rememberScrollState())
                        ) {
                            UserInfo()
                            UserOperateFeature()
                        }
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.uiAction.collect { uiEvent ->
                    when (uiEvent){
                        is UiEvent.GoLoginActEvent -> {
                            LoginActivity.start(this@SettingActivity)
                            finish()
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}