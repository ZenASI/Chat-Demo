package com.chat.joycom.ui.setting.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.HyperLinkTextDark
import com.chat.joycom.ui.theme.HyperLinkTextLight
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SafetyNotifyActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, SafetyNotifyActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Scaffold(
                    topBar = {
                        JoyComAppBar(
                            title = { Text(text = stringResource(id = R.string.safe_notify)) }
                        )
                    }
                ) { paddingValues ->
                    var isEnable by remember {
                        mutableStateOf(false)
                    }
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.size(20.dp))
                        Icon(
                            painterResource(id = R.drawable.ic_lock),
                            "",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = stringResource(id = R.string.safe_notify_desc))
                        genItemList().forEach { item ->
                            IconTextH(
                                icon = {
                                    Icon(
                                        painterResource(id = item.first),
                                        "",
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(id = item.second),
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                textFullWeightEnable = true,
                                height = 40.dp
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.learn_more),
                            modifier = Modifier
                                .align(Alignment.Start)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse("https://www.google.com/")
                                    startActivity(intent)
                                },
                            color = if (isSystemInDarkTheme()) HyperLinkTextDark else HyperLinkTextLight
                        )

                        Spacer(modifier = Modifier.size(20.dp))

                        IconTextH(
                            text = {
                                Column {
                                    Text(text = stringResource(id = R.string.device_show_safe_notify))
                                    Spacer(modifier = Modifier.size(20.dp))
                                    val annotatedString = buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                            append(getString(R.string.device_show_safe_notify_desc))
                                        }

                                        pushStringAnnotation(
                                            tag = "safe_notify_learn_more",
                                            annotation = "https://www.google.com/"
                                        )
                                        withStyle(
                                            style = SpanStyle(
                                                color = if (isSystemInDarkTheme()) HyperLinkTextDark else HyperLinkTextLight
                                            )
                                        ) {
                                            append(getString(R.string.learn_more))
                                        }
                                        pop()
                                    }
                                    ClickableText(
                                        text = annotatedString,
                                        onClick = { offset ->
                                            annotatedString.getStringAnnotations(
                                                tag = "safe_notify_learn_more",
                                                start = offset,
                                                end = offset
                                            ).firstOrNull()?.let {
                                                val intent = Intent(Intent.ACTION_VIEW)
                                                intent.data = Uri.parse(it.item)
                                                startActivity(intent)
                                            }
                                        }
                                    )
                                }
                            },
                            action = {
                                Switch(checked = isEnable, onCheckedChange = { isEnable = it })
                            },
                            textFullWeightEnable = true
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }

    private fun genItemList() =
        listOf(
            Pair(R.drawable.ic_chat, R.string.text_and_audio_msg),
            Pair(R.drawable.ic_phone, R.string.audio_and_video_call),
            Pair(R.drawable.ic_file, R.string.photo_and_video_and_file),
            Pair(R.drawable.ic_location, R.string.share_location),
            Pair(R.drawable.ic_storage, R.string.dynamic_update),
        )
}