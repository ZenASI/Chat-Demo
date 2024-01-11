package com.chat.joycom.ui.web

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.viewinterop.AndroidView
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.DropdownColumn
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.commom.WebViewUrl
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            var isExpanded by remember {
                mutableStateOf(false)
            }
            val clipboardManager = LocalClipboardManager.current
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            JoyComTheme {
                Scaffold(
                    topBar = {
                        JoyComAppBar(
                            title = { Text(text = stringResource(id = R.string.common_problem_of_contact)) },
                            acton = {
                                Box {
                                    TopBarIcon(R.drawable.ic_more_vert, onClick = {isExpanded = true})
                                    DropdownColumn(
                                        showState = isExpanded,
                                        onDismissRequest = { isExpanded = false },
                                        itemList = listOf(
                                            R.string.open_in_browser,
                                            R.string.share,
                                            R.string.copy_link
                                        ),
                                        itemClick = { stringRes ->
                                            when (stringRes) {
                                                R.string.open_in_browser -> {
                                                    val intent = Intent(Intent.ACTION_VIEW)
                                                    intent.data = Uri.parse(url)
                                                    startActivity(intent)
                                                }

                                                R.string.share -> {
                                                    val sendIntent: Intent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(Intent.EXTRA_TEXT, url)
                                                        type = "text/plain"
                                                    }
                                                    val shareIntent =
                                                        Intent.createChooser(sendIntent, null)
                                                    startActivity(shareIntent)
                                                }

                                                R.string.copy_link -> {
                                                    url?.let {
                                                        scope.launch {
                                                            clipboardManager.setText(AnnotatedString(it))
                                                            snackBarHostState.showSnackbar(getString(R.string.copy_link_success))
                                                        }
                                                    }
                                                }
                                            }
                                            isExpanded = false
                                        }
                                    )
                                }
                            },
                        )
                    },
                    snackbarHost = { SnackbarHost(snackBarHostState) }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
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