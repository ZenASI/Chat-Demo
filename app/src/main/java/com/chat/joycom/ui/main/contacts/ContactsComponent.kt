package com.chat.joycom.ui.main.contacts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.ext.startShareIntent
import com.chat.joycom.ui.commom.DropdownColumn
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.main.contacts.add.contacts.AddContactActivity
import com.chat.joycom.ui.main.contacts.add.group.NewGroupActivity
import com.chat.joycom.ui.setting.qrcode.QRCodeActivity
import com.chat.joycom.ui.web.WebActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContactsTopBarActions(viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var showLoading by remember {
        mutableStateOf(false)
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = showLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(30.dp),
                strokeWidth = 2.dp
            )
        }
        TopBarIcon(
            R.drawable.ic_search,
            onClick = {
                viewModel.showSearchBool =
                    viewModel.showSearchBool.not()
            }
        )
        Box(modifier = Modifier) {
            TopBarIcon(R.drawable.ic_more_vert, onClick = { isExpanded = true })
            val menuList by remember {
                mutableStateOf(
                    listOf(
                        R.string.invite_friends,
                        R.string.contacts,
                        R.string.refresh,
                        R.string.help
                    )
                )
            }
            DropdownColumn(
                showState = isExpanded,
                onDismissRequest = { isExpanded = false },
                itemList = menuList,
                itemClick = {
                    isExpanded = false
                    when (it) {
                        R.string.refresh -> {
                            scope.launch {
                                showLoading = true
                                delay(2000)
                                showLoading = false
                            }
                        }

                        R.string.invite_friends -> {
                            context.startShareIntent()
                        }

                        R.string.help -> {
                            WebActivity.start(
                                context,
                                "https://faq.whatsapp.com/1183494482518500?locale=zh_TW"
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ContactsColumn(modifier: Modifier = Modifier, viewModel: ContactsViewModel = viewModel()) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        item {
            ContactTopColumn()
        }
        item {
            ContactMergeColumn()
        }
        item {
            ContactBottomColumn()
        }
    }
}

@Composable
fun ContactMergeColumn(viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    val onContactList = remember {
        viewModel.onContactList
    }

    val inviteList = remember {
        viewModel.inviteList
    }

    val queryList = viewModel.searchContactList.collectAsState()

    Text(
        text = stringResource(id = R.string.on_joycom_contact),
        modifier = Modifier.padding(horizontal = 10.dp)
    )
    onContactList.forEach {
        IconTextH(
            icon = {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.avatar)
                        .crossfade(true).build(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.ic_def_user),
                    error = painterResource(id = R.drawable.ic_def_user),
                    contentScale = ContentScale.Crop
                )
            },
            text = { Text(text = it.nickname, fontSize = 20.sp) },
            modifier = Modifier
                .clickable { }
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
    }

    Text(
        text = stringResource(id = R.string.invite_to_use_joycom),
        modifier = Modifier.padding(horizontal = 10.dp)
    )
    inviteList.forEach {
        IconTextH(
            icon = {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.avatar)
                        .crossfade(true).build(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.ic_def_user),
                    error = painterResource(id = R.drawable.ic_def_user),
                    contentScale = ContentScale.Crop
                )
            },
            text = { Text(text = it.nickname, fontSize = 20.sp) },
            action = {
                Button(
                    onClick = {
                        context.startShareIntent()
                    },
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.invite))
                }
            },
            spaceWeightEnable = Pair(false, true),
            modifier = Modifier
                .clickable {
                    context.startShareIntent()
                }
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun ContactTopColumn(viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    val featureList by remember {
        mutableStateOf(
            listOf(
                Pair(R.string.new_group, R.drawable.ic_group2),
                Pair(R.string.add_new_contact, R.drawable.ic_add_person),
                Pair(R.string.new_community, R.drawable.ic_group3),
            )
        )
    }
    val inputText = viewModel.searchInputText.collectAsState()
    if (inputText.value.isEmpty()) {
        Column {
            featureList.forEachIndexed { index, pairItem ->
                IconTextH(
                    icon = {
                        Image(
                            painterResource(
                                id = pairItem.second
                            ),
                            "",
                            modifier = Modifier
                                .size(40.dp)
                        )
                    },
                    text = {
                        Text(text = stringResource(id = pairItem.first), fontSize = 20.sp)
                    },
                    action = {
                        if (index == 1) {
                            Icon(
                                painterResource(id = R.drawable.ic_qr_code),
                                "",
                                modifier = Modifier
                                    .clickable {
                                        QRCodeActivity.start(context)
                                    }
                                    .size(40.dp)
                            )
                        }
                    },
                    spaceWeightEnable = Pair(false, true),
                    modifier = Modifier
                        .clickable {
                            when (index) {
                                0 -> {
                                    NewGroupActivity.start(context)
                                }

                                1 -> {
                                    AddContactActivity.start(context)
                                }

                                2 -> {

                                }
                            }
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    )
            }
        }
    }
}

@Composable
fun ContactBottomColumn(viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    Column {
        if (viewModel.showSearchBool) {
            Text(text = stringResource(id = R.string.more), Modifier.padding(horizontal = 10.dp))
            IconTextH(
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_add_person),
                        "",
                        modifier = Modifier
                            .size(50.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.add_new_contact),
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier
                    .clickable { AddContactActivity.start(context) }
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
        }
        IconTextH(
            icon = {
                Icon(
                    Icons.Filled.Share,
                    "",
                    modifier = Modifier
                        .size(40.dp)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.share_invite_link),
                    fontSize = 20.sp
                )
            },
            modifier = Modifier
                .clickable {
                    context.startShareIntent()
                }
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
        IconTextH(
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_question_mark),
                    "",
                    modifier = Modifier
                        .size(40.dp)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.common_problem_of_contact),
                    fontSize = 20.sp
                )
            },
            modifier = Modifier
                .clickable {
                    WebActivity.start(
                        context,
                        "https://faq.whatsapp.com/1183494482518500?locale=zh_TW"
                    )
                }
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
    }
}