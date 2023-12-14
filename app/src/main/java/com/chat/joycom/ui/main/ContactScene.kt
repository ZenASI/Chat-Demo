package com.chat.joycom.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.model.Contact
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl

@Composable
fun ContactScene() {

    val viewModel: MainActivityViewModel = viewModel()
    val contacts = viewModel.contacts.collectAsState(initial = mutableListOf()).value

    Scaffold { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(
                items = contacts,
                key = { item: Contact -> item.userId },
                contentType = { item: Contact -> item.userId }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(5.dp)),
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
                        contentScale = ContentScale.Crop,
                    )

                    Text(text = item.nickname)
                }
            }
        }
    }
}