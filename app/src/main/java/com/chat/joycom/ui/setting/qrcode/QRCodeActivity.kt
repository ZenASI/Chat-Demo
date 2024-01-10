package com.chat.joycom.ui.setting.qrcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QRCodeActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, QRCodeActivity::class.java)
            )
        }
    }

    private val viewModel by viewModels<QRCodeViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                val tabList = QRCodeScene.values().toList()
                val scope = rememberCoroutineScope()
                val pagerState = rememberPagerState { tabList.size }
                Scaffold(topBar = {
                    Column {
                        JoyComAppBar(
                            title = { Text(text = stringResource(id = R.string.qrcode)) },
                            acton = { if (pagerState.currentPage == 0) QrcodeTopBarAction() }
                        )
                        QrcodeTabRow(
                            currentScene = tabList[pagerState.currentPage],
                            onClick = { ordinal ->
                                scope.launch { pagerState.animateScrollToPage(ordinal) }
                            })
                    }
                }) { paddingValues ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        tabList[it].body.invoke()
                    }
                }
            }
        }
    }
}