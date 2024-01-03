package com.chat.joycom.ui.setting.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.TextFieldSheet
import com.chat.joycom.ui.setting.user.about.AboutMeActivity

@Composable
fun UserInfoHeader(
    modifier: Modifier = Modifier,
    viewModel: UserInfoViewModel = viewModel(),
    showBottomSheet: (Boolean) -> Unit
) {
    val memberInfo = viewModel.memberInfo.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .clickable {
                    showBottomSheet.invoke(true)
                }
        ) {
            Image(
                painterResource(id = R.drawable.ic_def_user),
                "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(180.dp)
            )
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            ) {
                Image(
                    painterResource(id = R.drawable.ic_camera),
                    "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun UserInfoList(modifier: Modifier = Modifier, viewModel: UserInfoViewModel = viewModel()) {
    val context = LocalContext.current
    val memberInfo = viewModel.memberInfo.collectAsState()
    var showEditName by remember {
        mutableStateOf(false)
    }
    val about = viewModel.currentAbout.collectAsState(initial = null).value
    Column(modifier = modifier) {
        IconTextH(
            icon = {
                Box(modifier = Modifier.size(60.dp)) {
                    Image(
                        painterResource(id = R.drawable.ic_def_user),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            text = {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.name))
                    memberInfo.value?.let {
                        Spacer(modifier = Modifier.size(2.dp))
                        Text(text = it.nickname)
                    }
                }
            },
            action = {
                Box(modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        showEditName = true
                    }) {
                    Image(
                        Icons.Filled.Create,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        )
        IconTextH(
            icon = {
                Box(modifier = Modifier.size(60.dp)) {
                    Image(
                        painterResource(id = R.drawable.ic_def_user),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            text = {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.about_me))
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(text = about?.aboutText ?: "")
                }
            },
            action = {
                Box(modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        AboutMeActivity.start(context)
                    }) {
                    Image(
                        Icons.Filled.Create,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        )
        IconTextH(
            icon = {
                Box(modifier = Modifier.size(60.dp)) {
                    Image(
                        Icons.Filled.Phone,
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            text = {
                Column {
                    Text(text = stringResource(id = R.string.phone))
                    Text(text = viewModel.phone.value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        )
    }
    if (showEditName) {
        TextFieldSheet(
            name = memberInfo.value?.nickname ?: "",
            title = R.string.pls_input_your_name,
            showState = { showEditName = it },
            onText = {})
    }
}