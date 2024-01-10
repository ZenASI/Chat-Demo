package com.chat.joycom.ui.commom

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewUrl(url:String?, modifier: Modifier = Modifier){
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
            }
        },
        update = { webView ->
            webView.loadUrl(url ?: "https://www.google.com/")
        }
    )
}