package com.chat.joycom.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.main.MainActivityViewModel
import com.chat.joycom.ui.setting.account.AccountActivity
import com.chat.joycom.ui.setting.qrcode.QRCodeActivity
import com.chat.joycom.ui.setting.user.UserInfoActivity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfo() {
    val context = LocalContext.current
    val viewModel: MainActivityViewModel = viewModel()
    val member = viewModel.memberInfo.collectAsState(initial = null).value
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                UserInfoActivity.start(context)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(UrlPath.GET_FILE.getFileFullUrl() + member?.avatar)
                .crossfade(true).build(),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 10.dp)
                .width(70.dp)
                .height(70.dp)
                .align(Alignment.CenterVertically),
            placeholder = painterResource(id = R.drawable.ic_def_user),
            error = painterResource(id = R.drawable.ic_def_user),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = member?.nickname ?: "", fontSize = 20.sp)
            Spacer(modifier = Modifier.size(15.dp))
            Text(text = stringResource(id = R.string.about_me_desc), fontSize = 14.sp)
        }
        Row() {
            Icon(
                painterResource(id = R.drawable.ic_qr_code),
                "",
                modifier = Modifier.clickable {
                    QRCodeActivity.start(context)
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Icon(
                Icons.Filled.KeyboardArrowDown,
                "",
                modifier = Modifier
                    .border(1.dp, Color.White, CircleShape)
                    .clickable { showBottomSheet = true }
            )
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                IconTextH(
                    icon = {
                        Image(
                            painterResource(id = R.drawable.ic_def_user),
                            "",
                            modifier = Modifier.size(50.dp)
                        )
                    },
                    text = { Text(member?.nickname ?: "", modifier = Modifier.weight(1f)) },
                    action = {
                        Image(
                            Icons.Filled.Check, "", modifier = Modifier.background(
                                MaterialTheme.colorScheme.primary, CircleShape
                            )
                        )
                    },
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                        }
                )
                IconTextH(
                    icon = { Icon(Icons.Filled.Add, "", modifier = Modifier.size(50.dp)) },
                    text = { Text(stringResource(id = R.string.add_new_account)) },
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                        }
                )
            }
        }
    }
}

@Composable
fun UserOperateFeature(viewModel: SettingViewModel = viewModel()) {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background),
    ) {
        genSettingFeaturesItem().forEachIndexed { index, item ->
            IconTextH(
                icon = {
                    Box(modifier = Modifier.size(60.dp)) {
                        Image(
                            painter = painterResource(id = item.third),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = stringResource(id = item.first), maxLines = 1)
                        if (stringResource(item.second).isNotEmpty()){
                            Spacer(modifier = Modifier.size(1.dp))
                            Text(text = stringResource(id = item.second), maxLines = 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (index) {
                            0 -> AccountActivity.start(context)
                        }
                    },
                horizontalArrangement = Arrangement.Start,
            )
        }
    }
}

private fun genSettingFeaturesItem() =
    listOf(
        Triple(R.string.account, R.string.account_desc, R.drawable.ic_key),
        Triple(R.string.privacy, R.string.privacy_desc, R.drawable.ic_lock),
        Triple(R.string.avatar, R.string.avatar_desc, R.drawable.ic_emoji),
        Triple(R.string.conversation, R.string.conversation_desc, R.drawable.ic_chat),
        Triple(R.string.notification, R.string.notification_desc, R.drawable.ic_notifications),
        Triple(R.string.storage, R.string.storage_desc, R.drawable.ic_storage),
        Triple(R.string.app_language, R.string.app_language_desc, R.drawable.ic_language),
        Triple(R.string.help, R.string.help_desc, R.drawable.ic_help),
        Triple(R.string.invite_friends, R.string.empty, R.drawable.ic_group2),
    )