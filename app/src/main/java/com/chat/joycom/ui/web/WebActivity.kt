package com.chat.joycom.ui.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.commom.WebViewUrl
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

private val WEB_URL = "WEB_URL"

@AndroidEntryPoint
class WebActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
            url: String
        ) {
            val intent = Intent(context, WebActivity::class.java).apply {
                putExtra(WEB_URL, url)
            }
            context.startActivity(intent)
        }
    }

    private val url by lazy {
        intent.getStringExtra(WEB_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Scaffold(
                    topBar = {
                        JoyComAppBar(
                            title = { Text(text = stringResource(id = R.string.common_problem_of_contact)) },
                            acton = { TopBarIcon(R.drawable.ic_more_vert, onClick = {}) }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background)
                    ) {
                        WebViewUrl(url = url)
                    }
                }
            }
        }
    }
}