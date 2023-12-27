package com.chat.joycom.ui.setting.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextV
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                        var showBottomSheet by remember { mutableStateOf(false) }
                        val sheetState = rememberModalBottomSheetState()
                        val scope = rememberCoroutineScope()
                        Column(modifier = Modifier.padding(paddingValues)) {
                            UserInfoHeader(showBottomSheet = { showBottomSheet = it })
                            UserInfoList()
                        }
                        if (showBottomSheet) {
                            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                                Column {
                                    Text(
                                        text = stringResource(id = R.string.self_icon),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 10.dp),
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.size(20.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        IconTextV(
                                            icon = { Icon(painterResource(id = R.drawable.ic_camera), "") },
                                            text = { Text(text = stringResource(id = R.string.camera)) },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    showBottomSheet = false
                                                }
                                        )
                                        IconTextV(
                                            icon = { Icon(painterResource(id = R.drawable.ic_image), "") },
                                            text = { Text(text = stringResource(id = R.string.gallery)) },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
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
                                        IconTextV(
                                            icon = { Icon(painterResource(id = R.drawable.ic_image), "") },
                                            text = { Text(text = stringResource(id = R.string.avatar)) },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
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
                                    Spacer(modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}