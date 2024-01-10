package com.chat.joycom.ui.main.contacts

import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.main.contacts.add.contacts.AddContactActivity
import com.chat.joycom.ui.main.contacts.add.group.NewGroupActivity
import com.chat.joycom.ui.setting.qrcode.QRCodeActivity
import com.chat.joycom.ui.theme.JoyComDropDownTheme
import com.chat.joycom.ui.web.WebActivity

@Composable
fun ContactsTopBarActions(viewModel: ContactsViewModel = viewModel()) {
    val context = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
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
            JoyComDropDownTheme {
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
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
    }
}

@Composable
fun ContactsColumn(modifier: Modifier = Modifier, viewModel: ContactsViewModel = viewModel()) {
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .imePadding()) {
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
    val onContactList = remember {
        viewModel.onContactList
    }

    val inviteList = remember {
        viewModel.inviteList
    }

    val searchText = remember {
        viewModel.searchInputText
    }

    val queryList = viewModel.searchContactList.collectAsState()

    Text(
        text = stringResource(id = R.string.on_joycom_contact),
        modifier = Modifier.padding(horizontal = 10.dp)
    )
    onContactList.forEach {
        IconTextH(
            icon = {
                Image(
                    painterResource(id = R.drawable.ic_def_user),
                    "",
                    modifier = Modifier
                        .size(40.dp)
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
                Image(
                    painterResource(id = R.drawable.ic_def_user),
                    "",
                    modifier = Modifier
                        .size(40.dp)
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
                R.string.new_group,
                R.string.add_new_contact,
                R.string.new_community,
            )
        )
    }
    val inputText = viewModel.searchInputText.collectAsState()
    if (inputText.value.isEmpty()) {
        Column {
            featureList.forEachIndexed { index, id ->
                IconTextH(
                    icon = {
                        Image(
                            painterResource(
                                id = R.drawable.ic_def_group
                            ),
                            "",
                            modifier = Modifier
                                .size(40.dp)
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
                            .size(40.dp)
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
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_content))
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
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
                    WebActivity.start(context, "https://faq.whatsapp.com/1183494482518500?locale=zh_TW")
                }
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
    }
}