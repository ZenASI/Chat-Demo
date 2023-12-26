package com.chat.joycom.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.login.LoginActivity
import com.chat.joycom.ui.main.MainActivityViewModel
import com.chat.joycom.ui.setting.user.UserInfoActivity
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : BaseActivity() {
    private val viewModel by viewModels<SettingViewModel>()

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, SettingActivity::class.java)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme() {
                Surface {
                    Scaffold(
                        topBar = {
                            JoyComAppBar(
                                showBack = true,
                                title = { Text(text = stringResource(id = R.string.setting)) },
                                acton = { Image(painterResource(id = R.drawable.ic_search), "") }
                            )
                        },
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .verticalScroll(rememberScrollState())
                        ) {
                            UserInfo()
                            UserOperateFeature()
                        }
                    }
                }
            }
        }
        initCollect()
    }

    private fun initCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it is UiEvent.GoLoginActEvent) {
                        LoginActivity.start(this@SettingActivity)
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfo() {
    val context = LocalContext.current
    val viewModel: MainActivityViewModel = viewModel()
    val member = viewModel.memberInfo.collectAsState(initial = null).value
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
            placeholder = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.size(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = member?.nickname ?: "", fontSize = 20.sp)
            Spacer(modifier = Modifier.size(15.dp))
            Text(text = stringResource(id = R.string.about_me_desc))
        }
    }
}

@Composable
fun UserOperateFeature() {
    val viewModel: SettingViewModel = viewModel()
    val settingFeatures = SettingFeatures.values().toList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background),
    ) {
        settingFeatures.forEach { item ->
            IconTextH(
                icon = {
                    Box(modifier = Modifier.size(60.dp)) {
                        Image(
                            painter = painterResource(id = item.icon),
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
                        Text(text = stringResource(id = item.title), maxLines = 1)
                        item.content?.let {
                            Spacer(modifier = Modifier.size(1.dp))
                            Text(text = stringResource(id = it), maxLines = 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clickable {

                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            )
        }
    }
}