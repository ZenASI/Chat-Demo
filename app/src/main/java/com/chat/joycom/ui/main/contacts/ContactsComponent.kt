package com.chat.joycom.ui.main.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.main.contacts.add.contacts.AddContactActivity
import com.chat.joycom.ui.main.contacts.add.group.NewGroupActivity
import com.chat.joycom.ui.setting.qrcode.QRCodeActivity

@Composable
fun ContactsTopBarActions() {
    val context = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Icon(painterResource(id = R.drawable.ic_search), "")
    Box(modifier = Modifier) {
        Icon(Icons.Filled.MoreVert, "", modifier = Modifier.clickable {
            isExpanded = true
        })
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
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

            menuList.forEach {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = it)) },
                    onClick = {
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ContactsColumn(modifier: Modifier = Modifier, viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    val featureList by remember {
        mutableStateOf(
            listOf(
                R.string.new_group,
                R.string.add_new_contact,
                R.string.new_community,
            )
        )
    }

    val onContactList = viewModel.onContactList

    val inviteList = viewModel.inviteList

    LazyColumn(modifier = modifier.fillMaxSize()) {
        featureList.forEachIndexed { index, id ->
            item {
                IconTextH(
                    icon = {
                        Image(
                            painterResource(
                                id = R.drawable.ic_def_group
                            ),
                            "",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(horizontal = 10.dp)
                        )
                    },
                    text = {
                        Text(text = stringResource(id = id), fontSize = 20.sp)
                    },
                    action = {
                        if (index == 1) {
                            Image(
                                painterResource(id = R.drawable.ic_qr_code),
                                "",
                                modifier = Modifier
                                    .clickable {
                                        QRCodeActivity.start(context)
                                    }
                                    .size(60.dp)
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    },
                    spaceWeightEnable = Pair(false, true),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
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
                        },
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.on_joycom_contact),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        onContactList.forEach {
            item {
                IconTextH(
                    icon = {
                        Image(
                            painterResource(id = R.drawable.ic_def_user),
                            "",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(horizontal = 10.dp)
                        )
                    },
                    text = { Text(text = it.nickname, fontSize = 20.sp) },
                    modifier = Modifier
                        .clickable { }
                        .fillMaxWidth()
                        .height(70.dp)
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.invite_to_use_joycom),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        inviteList.forEach {
            item {
                IconTextH(
                    icon = {
                        Image(
                            painterResource(id = R.drawable.ic_def_user),
                            "",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(horizontal = 10.dp)
                        )
                    },
                    text = { Text(text = it.nickname, fontSize = 20.sp) },
                    action = {
                        Button(
                            onClick = {},
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Text(text = stringResource(id = R.string.invite))
                        }
                    },
                    spaceWeightEnable = Pair(false, true),
                    modifier = Modifier
                        .clickable { }
                        .fillMaxWidth()
                        .height(70.dp)
                )
            }
        }
        item {
            IconTextH(
                icon = {
                    Icon(
                        Icons.Filled.Share,
                        "",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(horizontal = 10.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.share_invite_link),
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier
                    .clickable { }
                    .fillMaxWidth()
                    .height(70.dp)
            )
        }
        item {
            IconTextH(
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_question_mark),
                        "",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(horizontal = 10.dp)
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.common_problem_of_contact),
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier
                    .clickable { }
                    .fillMaxWidth()
                    .height(70.dp)
            )
        }
    }
}