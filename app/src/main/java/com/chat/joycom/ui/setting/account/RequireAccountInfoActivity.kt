package com.chat.joycom.ui.setting.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.HyperLinkTextDark
import com.chat.joycom.ui.theme.HyperLinkTextLight
import com.chat.joycom.ui.theme.JoyComTheme

class RequireAccountInfoActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, RequireAccountInfoActivity::class.java)
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
                            title = { Text(text = stringResource(id = R.string.require_account_info)) }
                        )
                    }
                ) { paddingValues ->
                    var requiredAccountInfo by remember {
                        mutableStateOf(false)
                    }
                    var requiredChannelReport by remember {
                        mutableStateOf(false)
                    }
                    Column(
                        modifier = Modifier.padding(paddingValues),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Spacer(modifier = Modifier.size(10.dp))
                        RequireAccountInfo(
                            required = requiredAccountInfo,
                            clickAction = {
                                requiredAccountInfo = true
                            },
                            textTitle = stringResource(id = R.string.account_info),
                            textBefore = stringResource(id = R.string.require_account_report),
                            textAfter = stringResource(id = R.string.require_account_sent),
                            remind = {
                                val annotatedString = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                        append(getString(R.string.account_info_report_note))
                                    }

                                    pushStringAnnotation(
                                        tag = "account_report_learn_more",
                                        annotation = "https://faq.whatsapp.com/526463418847093/?locale=zh_TW&cms_platform=android&eea=0&refsrc=deprecated&_rdr"
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
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    ),
                                    onClick = { offset ->
                                        annotatedString.getStringAnnotations(
                                            tag = "account_report_learn_more",
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
                        )
                        RequireAccountInfo(
                            required = requiredChannelReport,
                            clickAction = {
                                requiredChannelReport = true
                            },
                            textTitle = stringResource(id = R.string.channel_activity),
                            textBefore = stringResource(id = R.string.require_channel_activity_report),
                            textAfter = stringResource(id = R.string.require_account_sent),
                            remind = {
                                val annotatedString = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                        append(getString(R.string.channel_activity_report_note))
                                    }

                                    pushStringAnnotation(
                                        tag = "channel_activity_learn_more",
                                        annotation = "https://faq.whatsapp.com/526463418847093/?locale=zh_TW&cms_platform=android&eea=0&refsrc=deprecated&_rdr"
                                    )
                                    withStyle(
                                        style = SpanStyle(
                                            color = if (isSystemInDarkTheme()) HyperLinkTextDark else HyperLinkTextLight
                                        )
                                    ) {
                                        append(getString(R.string.learn_more_info))
                                    }
                                    pop()
                                }
                                ClickableText(
                                    text = annotatedString,
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    ),
                                    onClick = { offset ->
                                        annotatedString.getStringAnnotations(
                                            tag = "channel_activity_learn_more",
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
                        )
                    }
                }
            }
        }
    }
}
