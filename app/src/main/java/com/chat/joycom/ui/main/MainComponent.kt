package com.chat.joycom.ui.main

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.ui.chat.ChatActivity
import com.chat.joycom.ui.commom.BadgeView
import com.chat.joycom.ui.commom.DropdownColumn
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.InfoCardDialog
import com.chat.joycom.ui.commom.PermissionType
import com.chat.joycom.ui.commom.SimpleUrlImage
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.main.contacts.add.group.NewGroupActivity
import com.chat.joycom.ui.setting.SettingActivity
import com.chat.joycom.ui.theme.OnTabSelectDark
import com.chat.joycom.ui.theme.OnTabSelectLight
import com.chat.joycom.ui.theme.OnTabUnSelectDark
import com.chat.joycom.ui.theme.OnTabUnSelectLight
import com.chat.joycom.ui.theme.TabRowDark
import com.chat.joycom.ui.theme.TabRowLight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun CallScene(viewModel: MainActivityViewModel = viewModel()) {
    val list = remember {
        viewModel.phoneCallList
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        IconTextH(
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_link),
                    "",
                    modifier = Modifier.size(40.dp)
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.build_call_link), fontSize = 22.sp)
                    Text(text = stringResource(id = R.string.build_call_link_desc))
                }
            },
            textFullWeightEnable = true,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .clickable {}
                .padding(horizontal = 20.dp)
        )
        if (list.isEmpty()) {
            Text(
                text = stringResource(id = R.string.hint_phone_call_list_empty),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        } else {
            LazyColumn() {

            }
        }
    }
}

@Composable
fun CommunityScene(viewModel: MainActivityViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Image(painterResource(id = R.drawable.ic_bg), "")
        Text(
            text = stringResource(id = R.string.keep_community_linking),
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        Text(text = stringResource(id = R.string.keep_community_linking_desc))
        Text(text = stringResource(id = R.string.keep_community_linking_sample))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

            }
        ) {
            Text(text = stringResource(id = R.string.start_build_community))
        }
    }
}

@Composable
fun ChatScene(viewModel: MainActivityViewModel = viewModel()) {
    val listFlow = viewModel.combineFlow().collectAsState(initial = mutableListOf()).value
    val context = LocalContext.current
    val filterState = viewModel.filterState
    val searchText = viewModel.searchText
    if (filterState || searchText.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.no_search_result), modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            item {
                Spacer(modifier = Modifier.size(5.dp))
            }
            listFlow.forEachIndexed { index, item ->
                item(key = index) {
                    when (item) {
                        is Contact -> {
                            ContactMsgItem(item)
                        }

                        is Group -> {
                            GroupMsgItem(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateScene(viewModel: MainActivityViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

    }
}

@Composable
fun MainTableRow(currentScene: JoyComScene, onClick: (Int) -> Unit) {
    val joyComScenesList = JoyComScene.values().toList()
    val containerColor = if (isSystemInDarkTheme()) TabRowDark else TabRowLight
    val selectColor = if (isSystemInDarkTheme()) OnTabSelectDark else OnTabSelectLight
    val unSelectColor = if (isSystemInDarkTheme()) OnTabUnSelectDark else OnTabUnSelectLight
    TabRow(
        selectedTabIndex = currentScene.ordinal,
        divider = { },
        containerColor = containerColor,
        indicator = { tabPositions ->
            if (currentScene.ordinal < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.composed {
                        val currentTabWidth by animateDpAsState(
                            targetValue = tabPositions[currentScene.ordinal].width,
                            animationSpec = tween(
                                durationMillis = 50,
                                easing = FastOutSlowInEasing
                            ),
                            ""
                        )
                        val indicatorOffset by animateDpAsState(
                            targetValue = tabPositions[currentScene.ordinal].left,
                            animationSpec = tween(
                                durationMillis = 50,
                                easing = FastOutSlowInEasing
                            ),
                            ""
                        )
                        fillMaxWidth()
                            .wrapContentSize(Alignment.BottomStart)
                            .height(3.dp)
                            .offset(x = indicatorOffset)
                            .width(currentTabWidth)
                    },
//                    1.dp,
                    color = selectColor
                )
            }
        }
    ) {
        joyComScenesList.forEachIndexed { index, item ->
            Tab(
                selected = currentScene.ordinal == index,
                onClick = { onClick.invoke(index) },
                selectedContentColor = selectColor,
                unselectedContentColor = unSelectColor
            ) {
                if (index == 0) {
                    Icon(painterResource(id = R.drawable.ic_group3), "")
                } else {
                    Text(
                        text = stringResource(id = item.sceneName),
                        modifier = Modifier.padding(15.dp),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainTopBarAction(pagerState: PagerState, viewModel: MainActivityViewModel = viewModel()) {
    var showPermissionDesc by remember {
        mutableStateOf(false)
    }
    var permissionType by remember {
        mutableStateOf(PermissionType.StorageWithCamera)
    }
    val permissionState =
        rememberMultiplePermissionsState(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                )
            } else {
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        )

    var isExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
    ) {
        TopBarIcon(
            R.drawable.ic_camera,
            onClick = {},
        )

        AnimatedVisibility(
            visible = pagerState.currentPage != 0,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            TopBarIcon(
                R.drawable.ic_search,
                onClick = {
                    viewModel.searchState = viewModel.searchState.not()
                },
            )
        }
        Box {
            TopBarIcon(
                R.drawable.ic_more_vert,
                onClick = { isExpanded = true }
            )
            DropdownColumn(
                showState = isExpanded,
                onDismissRequest = { isExpanded = false },
                itemList = genMainDropdownMenuItem(pagerState.currentPage),
                itemClick = { stringRes ->
                    isExpanded = false
                    when (stringRes) {
                        R.string.setting -> SettingActivity.start(context)
                        R.string.create_group -> NewGroupActivity.start(context)
                        else -> {

                        }
                    }
                }
            )
        }
    }
}

private fun genMainDropdownMenuItem(page: Int) =
    if (page == 1) {
        listOf(
            R.string.create_group,
            R.string.create_broadcast,
            R.string.linked_device,
            R.string.marked_msg,
            R.string.setting,
        )
    } else {
        listOf(
            R.string.setting,
        )
    }

@Composable
fun ContactMsgItem(contact: Contact) {
    val context = LocalContext.current
    var infoShowState by remember {
        mutableStateOf(false)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                ChatActivity.start(context, contact = contact, isGroup = false)
            }
            .padding(horizontal = 10.dp, vertical = 20.dp)
    ) {
        SimpleUrlImage(
            url = contact.avatar,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .clickable {
                    infoShowState = true
                },
            placeholder = painterResource(id = R.drawable.ic_def_user),
            error = painterResource(id = R.drawable.ic_def_user),
        )
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Row {
                Text(text = contact.nickname, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "10:42")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.height(IntrinsicSize.Max)
            ) {
                Image(
                    painterResource(id = R.drawable.ic_read_checked),
                    "",
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.FillHeight
                )
                Text(
                    text = "Hi, hi~~~",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                BadgeView(1)
            }
        }
    }
    if (infoShowState) {
        InfoCardDialog(
            showState = { infoShowState = it },
            contact.nickname,
            imgUrl = contact.avatar,
            callBack = { infoShowState = false },
        )
    }
}

@Composable
private fun GroupMsgItem(group: Group) {
    var infoShowState by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                ChatActivity.start(context, group = group, isGroup = true)
            }
            .padding(horizontal = 10.dp, vertical = 20.dp)
    ) {
        SimpleUrlImage(
            url = group.avatar,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .clickable {
                    infoShowState = true
                },
            placeholder = painterResource(id = R.drawable.ic_def_group),
            error = painterResource(id = R.drawable.ic_def_group),
        )
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Row {
                Text(text = group.groupName, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "10:42")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.height(IntrinsicSize.Max)
            ) {
                Text(
                    text = "Hi, hi~~",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                BadgeView(100)
            }
        }
    }
    if (infoShowState) {
        InfoCardDialog(
            showState = { infoShowState = it },
            group.groupName,
            imgUrl = group.avatar,
            callBack = { infoShowState = false },
        )
    }
}


