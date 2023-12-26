package com.chat.joycom.ui.setting.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class UserInfoActivity : BaseActivity() {

    private val viewModel by viewModels<UserInfoViewModel>()

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, UserInfoActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Surface {
                    Scaffold(
                        topBar = {
                            JoyComAppBar(
                                showBack = true,
                                title = { Text(text = stringResource(id = R.string.user_info)) }
                            )
                        }
                    ) { paddingValues ->
                        val memberInfo = viewModel.memberInfo.collectAsState()
                        Column(modifier = Modifier.padding(paddingValues)) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                }) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .wrapContentSize()
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
                                            .background(MaterialTheme.colorScheme.primary, CircleShape)
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
                            Spacer(modifier = Modifier.size(50.dp))
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
                                verticalAlignment = Alignment.CenterVertically,
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
                                        Text(text = stringResource(id = R.string.about_me_desc))
                                    }
                                },
                                action = {
                                    Box(modifier = Modifier
                                        .size(60.dp)
                                        .clickable {

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
                                verticalAlignment = Alignment.CenterVertically,
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
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                            )
                        }
                    }
                }
            }
        }
    }
}