package com.chat.joycom.ui.main.contacts.add.group

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.IconTextV
import com.chat.joycom.ui.main.contacts.add.contacts.AddContactActivity

@Composable
fun NewGroupSelectContactRow(viewModel: NewGroupViewModel = viewModel()) {
    val onContactList = remember { viewModel.onContactList }
    val inviteList = remember { viewModel.inviteList }
    val selectList by remember {
        derivedStateOf {
            val list =
                onContactList.filter { it.isSelect } + inviteList.filter { it.isSelect }
            viewModel.updateSelectList()
            list
        }
    }

    LazyRow(
        modifier = Modifier
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        selectList.forEach {
            item {
                IconTextV(
                    icon = {
                        Box(modifier = Modifier.size(55.dp)) {
                            Image(
                                painterResource(id = R.drawable.ic_def_user),
                                "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterStart)
                            )
                            Image(
                                Icons.Filled.Close,
                                "",
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(
                                        Color.Gray, CircleShape
                                    )
                            )
                        }

                    },
                    text = {
                        Text(
                            text = it.nickname,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier
                        .width(70.dp)
                        .clickable {
                            it.isSelect = it.isSelect.not()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                )
            }
        }
    }
}

@Composable
fun NewGroupColumnList(viewModel: NewGroupViewModel = viewModel()) {
    val context = LocalContext.current
    val onContactList = remember { viewModel.onContactList }
    val inviteList = remember { viewModel.inviteList }
    LazyColumn() {
        item {
            Text(
                text = stringResource(id = R.string.on_joycom_contact),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        onContactList.forEachIndexed { index, member ->
            item {
                IconTextH(
                    icon = {
                        Box(modifier = Modifier.size(55.dp)) {
                            Image(
                                painterResource(id = R.drawable.ic_def_user),
                                "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterStart)
                            )
                            if (member.isSelect) {
                                Image(
                                    Icons.Filled.Check,
                                    "",
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    },
                    text = {
                        Text(
                            text = member.nickname,
                            fontSize = 20.sp
                        )
                    },
                    modifier = Modifier
                        .clickable {
                            onContactList[index].isSelect = onContactList[index].isSelect.not()
                        }
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.invite_to_use_joycom),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        inviteList.forEachIndexed { index, member ->
            item {
                IconTextH(
                    icon = {
                        Box(modifier = Modifier.size(55.dp)) {
                            Image(
                                painterResource(id = R.drawable.ic_def_user),
                                "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.Center)
                            )
                            if (member.isSelect) {
                                Image(
                                    Icons.Filled.Check,
                                    "",
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    },
                    text = {
                        Text(
                            text = member.nickname,
                            fontSize = 20.sp
                        )
                    },
                    modifier = Modifier
                        .clickable {
                            inviteList[index].isSelect = inviteList[index].isSelect.not()
                        }
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                )
            }
        }
        item {
            IconTextH(
                icon = {
                    Image(
                        painterResource(id = R.drawable.ic_def_user),
                        "",
                        modifier = Modifier
                            .size(50.dp)
                    )
                },
                text = { Text(text = stringResource(id = R.string.add_new_contact)) },
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .clickable {
                        AddContactActivity.start(context)
                    }
                    .padding(horizontal = 10.dp),

                horizontalArrangement = Arrangement.spacedBy(10.dp),
            )
        }
    }
}