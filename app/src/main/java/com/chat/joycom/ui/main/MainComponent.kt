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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.chat.ChatActivity
import com.chat.joycom.ui.commom.PermissionType
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.setting.SettingActivity
import com.chat.joycom.ui.theme.JoyComDropDownTheme
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

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
        Text(text = stringResource(id = R.string.keep_community_linking), fontWeight = FontWeight.Bold, fontSize = 26.sp)
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
                                placeholder = painterResource(id = R.drawable.ic_def_user),
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
                                placeholder = painterResource(id = R.drawable.ic_def_group),
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
                    Icon(painterResource(id = R.drawable.ic_group), "")
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
fun MainTopBarAction(pagerState: PagerState) {
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
    val dropDownList by remember(pagerState) {
        derivedStateOf {
            if (pagerState.currentPage == 1) {
                MainDropDown.values().toList()
            } else {
                listOf(MainDropDown.Setting)
            }
        }
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
                onClick = {},
            )
        }
        Box {
            TopBarIcon(
                R.drawable.ic_more_vert,
                onClick = { isExpanded = true }
            )
            JoyComDropDownTheme {
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },

                    ) {
                    dropDownList.forEach {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = it.itemName)) },
                            onClick = {
                                isExpanded = false
                                when (it) {
                                    MainDropDown.Setting -> SettingActivity.start(context)

                                    else -> {

                                    }
                                }
                            })
                    }
                }
            }
        }
    }
}

