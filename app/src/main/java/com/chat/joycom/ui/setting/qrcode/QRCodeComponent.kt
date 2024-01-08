package com.chat.joycom.ui.setting.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.ui.commom.PermissionDescAlert
import com.chat.joycom.ui.commom.PermissionType
import com.chat.joycom.ui.commom.QrcodeCamera
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.theme.OnTabSelectDark
import com.chat.joycom.ui.theme.OnTabSelectLight
import com.chat.joycom.ui.theme.OnTabUnSelectDark
import com.chat.joycom.ui.theme.OnTabUnSelectLight
import com.chat.joycom.ui.theme.TabRowDark
import com.chat.joycom.ui.theme.TabRowLight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun MyQrcode(viewModel: QRCodeViewModel = viewModel()) {
    val memberInfo = viewModel.memberInfo.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .aspectRatio(1f)
            ) {
                Image(
                    painterResource(id = R.drawable.ic_def_user),
                    "",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(50.dp)
                        .offset(y = (-25).dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = memberInfo.value?.nickname ?: "")
                    Text(
                        text = stringResource(id = R.string.joycom_contact),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .fillMaxWidth(.6f)
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_qr_code),
                            "",
                            modifier = Modifier.aspectRatio(1f)
                        )
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.my_qrcode_notice),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQrcode() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    var showPermissionDesc by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(cameraPermissionState) {
        if (!cameraPermissionState.status.isGranted) {
            if (cameraPermissionState.status.shouldShowRationale) {
                showPermissionDesc = true
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            QrcodeCamera()
        }
    }
    if (showPermissionDesc) {
        PermissionDescAlert(
            type = PermissionType.Qrcode,
            showState = { showPermissionDesc = false },
            acceptCallback = {
                cameraPermissionState.launchPermissionRequest()
            }
        )
    }
}

@Composable
fun QrcodeTabRow(currentScene: QRCodeScene, onClick: (Int) -> Unit) {
    val tabList = QRCodeScene.values().toList()
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
                    Modifier.tabIndicatorOffset(tabPositions[currentScene.ordinal]),
//                    1.dp,
                    color = selectColor
                )
            }
        },
    ) {
        tabList.forEachIndexed { index, item ->
            Tab(
                selected = currentScene.ordinal == index,
                onClick = { onClick.invoke(index) },
                selectedContentColor = selectColor,
                unselectedContentColor = unSelectColor
            ) {
                Text(text = stringResource(id = item.title), modifier = Modifier.padding(15.dp))
            }
        }
    }
}

@Composable
fun QrcodeTopBarAction() {
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        TopBarIcon(R.drawable.ic_share, onClick = {
//            val sendIntent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_STREAM, "uri")
//                type = "image/jpeg"
//            }
//            val shareIntent = Intent.createChooser(sendIntent, null)
//            startActivity(shareIntent)
        })
        TopBarIcon(R.drawable.ic_more_vert, onClick = {})
    }
}