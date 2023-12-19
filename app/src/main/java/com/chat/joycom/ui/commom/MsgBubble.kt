package com.chat.joycom.ui.commom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.model.Message
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.chat.ChatViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelfMsg(isDailyFirstMsg: Boolean, message: Message) {
    val viewModel: ChatViewModel = viewModel()
    val memberInfo = viewModel.memberInfo.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // first time show or hide
        if (isDailyFirstMsg) {
            Text(
                text = message.sendTime,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp, topEnd = 8.dp))
                .padding(top = 3.dp)
                .background(Color.Green.copy(alpha = .5f))
                .align(Alignment.End),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // replay
            // TODO: replay layout
            // content
            Text(
                text = message.content,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 3.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {}
                    )
            )
            // image or not
            // TODO: image from user

            // time and read
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = message.sendTime, modifier = Modifier.wrapContentWidth(Alignment.End), maxLines = 1)
                Icon(Icons.Filled.Check, "")
            }
        }
    }
}

@Preview
@Composable
fun SelfMsg_Preview() {
    val isDailyFirstMsg = true
    val message = Message.getFakeMessage()
    SelfMsg(isDailyFirstMsg = isDailyFirstMsg, message = message)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OtherMsg(isDailyFirstMsg: Boolean, message: Message) {
    val viewModel: ChatViewModel = viewModel()
    val fromUserId = remember { mutableLongStateOf(message.fromUserId) }

    val groupContactList = viewModel.groupContactList.collectAsState(initial = mutableListOf())
    val groupContact = remember(fromUserId, groupContactList) {
        derivedStateOf {
            groupContactList.value.firstOrNull { it.userId == fromUserId.longValue }
        }
    }
    val contact = viewModel.contactInfo.collectAsState()

    val nickName = remember(groupContact, contact) {
        derivedStateOf {
            if (message.isGroup) groupContact.value?.nickname else contact.value?.nickname
        }
    }
    val phone = remember(groupContact, contact) {
        derivedStateOf {
            if (message.isGroup) "" else "+"+ contact.value?.countryCode + contact.value?.phoneNumber
        }
    }

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // first time show or hide
        if (isDailyFirstMsg) {
            Text(
                text = message.sendTime,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )
        }
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 3.dp)
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(UrlPath.GET_FILE.getFileFullUrl() + if (message.isGroup) groupContact.value?.avatar else contact.value?.avatar)
                    .crossfade(true).build(),
                contentDescription = "",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape)
                    .align(Alignment.Top)
                    .clickable {
                        // TODO: show user info card
                    },
                placeholder = painterResource(id = R.drawable.ic_def_user),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_def_user)
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(Color.DarkGray.copy(alpha = .5f))
                    .fillMaxWidth(.9f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                // reply
                // TODO: reply layout
                // name and phone
                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = nickName.value ?: "", maxLines = 1, textAlign = TextAlign.Start, modifier = Modifier)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = phone.value, maxLines = 1, textAlign = TextAlign.End, modifier = Modifier)
                }
                // image or not
                // TODO: image from user
                // content
                Text(
                    text = message.content,
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .align(Alignment.Start)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {}
                        )
                )
                // time
                Text(
                    text = message.sendTime,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 3.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun OtherMsg_PreView() {
    val isDailyFirstMsg = true
    val message = Message.getFakeMessage()
    OtherMsg(isDailyFirstMsg = isDailyFirstMsg, message = message)
}