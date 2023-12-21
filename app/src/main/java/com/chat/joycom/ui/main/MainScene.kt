package com.chat.joycom.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.chat.ChatActivity

@Composable
fun CallScene(viewModel: MainActivityViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Call", textAlign = TextAlign.Center, modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Composable
fun GroupScene(viewModel: MainActivityViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Group", textAlign = TextAlign.Center, modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Composable
fun ChatScene(viewModel: MainActivityViewModel = viewModel()) {
    val listFlow = viewModel.combineFlow().collectAsState(initial = mutableListOf()).value
    val context = LocalContext.current
    Scaffold { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            listFlow.forEachIndexed { index, item ->
                item(key = index) {
                    when (item) {
                        is Contact -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        ChatActivity.start(context, item, null, false)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(UrlPath.GET_FILE.getFileFullUrl() + item.avatar)
                                        .crossfade(true).build(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .width(50.dp)
                                        .height(50.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.CenterVertically),
                                    placeholder = null,
                                    error = painterResource(id = R.drawable.ic_def_user),
                                    contentScale = ContentScale.Crop,
                                )
                                Text(text = item.nickname)
                            }
                        }

                        is Group -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        ChatActivity.start(context, null, item, true)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(UrlPath.GET_FILE.getFileFullUrl() + item.avatar)
                                        .crossfade(true).build(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .width(50.dp)
                                        .height(50.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.CenterVertically),
                                    placeholder = null,
                                    error = painterResource(id = R.drawable.ic_def_group),
                                    contentScale = ContentScale.Crop,
                                )
                                Text(text = item.groupName)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateScene(viewmode: MainActivityViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Update", textAlign = TextAlign.Center, modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

