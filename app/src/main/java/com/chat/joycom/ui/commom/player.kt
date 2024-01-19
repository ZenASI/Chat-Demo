package com.chat.joycom.ui.commom

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun SimpleUrlPlayer(url: String, showController: Boolean = false) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val defaultDataSourceFactory = DefaultDataSource.Factory(context)
            val source = ProgressiveMediaSource.Factory(defaultDataSourceFactory).createMediaSource(
                MediaItem.fromUri(url)
            )

            setMediaSource(source)
            prepare()
        }
    }

    AndroidView(factory = {
        PlayerView(context).apply {
            if (showController) {
                showController()
                useController = true
            } else {
                hideController()
                useController = false
            }

            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

            player = exoPlayer
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    })

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }
}