package com.chat.joycom.ui.commom

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.chat.joycom.R

@Composable
fun SimpleUrlImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    urlType: UrlType = UrlType.Image,
    error: Painter? = null,
    placeholder: Painter? = null
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .error(R.drawable.ic_image)
                .decoderFactory(
                    if (urlType == UrlType.Image) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            ImageDecoderDecoder.Factory()
                        } else {
                            GifDecoder.Factory()
                        }
                    } else {
                        VideoFrameDecoder.Factory()
                    }
                )
                .crossfade(true)
                .videoFrameMillis(2000)
                .build(),
            contentDescription = "",
            modifier = modifier,
            contentScale = contentScale,
            error = error,
            placeholder = placeholder,
        )
        AnimatedVisibility(
            visible = urlType == UrlType.VideoFrame, modifier = Modifier.align(
                Alignment.Center
            )
        ) {
            Image(
                painterResource(id = R.drawable.ic_play_circle),
                "",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

enum class UrlType {
    Image,
    VideoFrame
}