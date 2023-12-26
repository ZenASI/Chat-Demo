package com.chat.joycom.ui.setting.qrcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Surface {
                    val tabList = QRCodeScene.values().toList()
                    val scope = rememberCoroutineScope()
                    val pagerState = rememberPagerState { tabList.size }
                    Scaffold(topBar = {
                        Column {
                            JoyComAppBar(
                                showBack = true,
                                title = { Text(text = stringResource(id = R.string.qrcode)) },
                                acton = {
                                    Icon(Icons.Filled.Share, "", modifier = Modifier.clickable {
//                                        val sendIntent = Intent().apply {
//                                            action = Intent.ACTION_SEND
//                                            putExtra(Intent.EXTRA_STREAM, "uri")
//                                            type = "image/jpeg"
//                                        }
//                                        val shareIntent = Intent.createChooser(sendIntent, null)
//                                        startActivity(shareIntent)
                                    })
                                    Icon(Icons.Filled.MoreVert, "", modifier = Modifier.clickable {

                                    })
                                }
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
}