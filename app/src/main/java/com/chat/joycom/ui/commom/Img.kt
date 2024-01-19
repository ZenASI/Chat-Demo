package com.chat.joycom.ui.commom

import android.os.Build
import android.os.ext.SdkExtensions
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.chat.joycom.R
import timber.log.Timber

@Composable
fun SimpleUrlImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Inside,
    urlType: UrlType = UrlType.Image
) {
    Box(modifier = Modifier.wrapContentSize()) {
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
        )
        AnimatedVisibility(
            visible = urlType == UrlType.VideoFrame, modifier = Modifier.align(
                Alignment.Center
            )
        ) {
            Image(painterResource(id = R.drawable.ic_play_circle), "", modifier = Modifier.size(30.dp))
        }
    }
}

enum class UrlType {
    Image,
    VideoFrame
}