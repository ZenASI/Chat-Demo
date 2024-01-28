package com.chat.joycom.ui.commom

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun SimpleDataImage(
    data: Any,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    urlType: UrlType = UrlType.Image,
    error: Painter? = null,
    placeholder: Painter? = null
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageVGridBySAF(
    imageIdList: MutableList<Long>,
    modifier: Modifier = Modifier,
    pickUri: ((Uri) -> Unit)? = null,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        imageIdList.forEach { id ->
            item(key = id) {
                SimpleDataImage(
                    data = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    ),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .combinedClickable(onClick = {}, onLongClick = {}),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageFolderVGridBySAF(
    imageFolderMap: MutableMap<String, MutableList<Long>>,
    modifier: Modifier = Modifier,
    pickUri: ((Uri) -> Unit)? = null,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        imageFolderMap.forEach { name, longs ->
            item(key = name) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .combinedClickable(onClick = {}, onLongClick = {}),
                ) {
                    SimpleDataImage(
                        data = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            longs.first()
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillWidth
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black,
                                    )
                                )
                            )
                            .padding(horizontal = 5.dp)
                    ) {
                        Text(text = name, color = Color.White)
                        Text(text = longs.size.toString(), color = Color.White)
                    }
                }
            }
        }
    }
}