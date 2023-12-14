package com.chat.joycom.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.theme.JoyComTheme

@Composable
fun MyScene() {
    val viewModel: MainActivityViewModel = viewModel()
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            UserInfo()
            UserOperateFeature()
        }
    }
}

@Composable
fun UserInfo() {
    val viewModel: MainActivityViewModel = viewModel()
    val member = viewModel.memberInfo.collectAsState(initial = null).value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(UrlPath.GET_FILE.getFileFullUrl() + member?.avatar)
                .crossfade(true).build(),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 10.dp)
                .width(50.dp)
                .height(50.dp)
                .align(Alignment.CenterVertically),
            placeholder = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = member?.nickname ?: "")
            Spacer(modifier = Modifier.size(15.dp))
            Text(text = member?.userId.toString())
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun UserInfo_Preview_Light() {
    JoyComTheme() {
        UserInfo()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun UserInfo_Preview_Night() {
    JoyComTheme {
        UserInfo()
    }
}

@Composable
fun UserOperateFeature() {
    val viewModel: MainActivityViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OperateItem(Icons.Outlined.Phone, "更改綁定手機號", modifier = Modifier.clickable {

        })
        OperateItem(Icons.Outlined.Lock, "設置pin碼", modifier = Modifier.clickable {

        })
        OperateItem(Icons.Outlined.Delete, "帳號註銷", modifier = Modifier.clickable {

        })
        OperateItem(Icons.Outlined.ExitToApp, "退出登入", modifier = Modifier.clickable {
            viewModel.logout()
        })
    }
}

@Composable
fun OperateItem(vector: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Icon(
            vector,
            "",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
        )
        Text(text, modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.KeyboardArrowRight,
            "",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun OperateItem_Preview_Light() {
    JoyComTheme {
        OperateItem(Icons.Outlined.Phone, stringResource(id = R.string.app_name))
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OperateItem_Preview_Night() {
    JoyComTheme {
        OperateItem(Icons.Outlined.Phone, stringResource(id = R.string.app_name))
    }
}