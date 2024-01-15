package com.chat.joycom.ui.main.contacts.add.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.HyperLinkTextDark
import com.chat.joycom.ui.theme.HyperLinkTextLight
import com.chat.joycom.ui.theme.JoyComTheme
import timber.log.Timber

class GroupPermissionActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, GroupPermissionActivity::class.java)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Surface {
                    Scaffold(
                        topBar = {
                            JoyComAppBar(
                                title = { Text(text = stringResource(id = R.string.group_permission)) }
                            )
                        }
                    ) { paddingValues ->
                        var editEnable by remember {
                            mutableStateOf(true)
                        }
                        var sendEnable by remember {
                            mutableStateOf(true)
                        }
                        var addMemberEnable by remember {
                            mutableStateOf(true)
                        }
                        var adminAllowEnable by remember {
                            mutableStateOf(false)
                        }
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = stringResource(id = R.string.group_permission_desc))
                            IconTextH(
                                icon = { Icon(Icons.Filled.Edit, "") },
                                text = {
                                    Column {
                                        Text(text = stringResource(id = R.string.edit_group_config))
                                        Text(text = stringResource(id = R.string.edit_group_config_desc))
                                    }
                                },
                                action = {
                                    Switch(
                                        checked = editEnable,
                                        onCheckedChange = { editEnable = it })
                                },
                                textFullWeightEnable = true,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            )
                            IconTextH(
                                icon = { Icon(painterResource(id = R.drawable.ic_message), "") },
                                text = { Text(text = stringResource(id = R.string.send_msg)) },
                                action = {
                                    Switch(
                                        checked = sendEnable,
                                        onCheckedChange = { sendEnable = it })
                                },
                                spaceWeightEnable = Pair(false, true),
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            )
                            IconTextH(
                                icon = { Image(painterResource(id = R.drawable.ic_add_person), "") },
                                text = { Text(text = stringResource(id = R.string.add_another_member)) },
                                action = {
                                    Switch(
                                        checked = addMemberEnable,
                                        onCheckedChange = { addMemberEnable = it })
                                },
                                spaceWeightEnable = Pair(false, true),
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            )
                            Text(text = stringResource(id = R.string.admin_has))
                            IconTextH(
                                icon = { Image(painterResource(id = R.drawable.ic_add_person), "") },
                                text = {
                                    Column {
                                        val annotatedString = buildAnnotatedString {
                                            withStyle(SpanStyle(MaterialTheme.colorScheme.onSurface)){
                                                append(stringResource(id = R.string.allow_add_new_member_desc))
                                            }
                                            withStyle(style = SpanStyle(if (isSystemInDarkTheme()) HyperLinkTextDark else HyperLinkTextLight)) {
                                                append(stringResource(id = R.string.learn_more))
                                            }
                                        }
                                        Text(text = stringResource(id = R.string.allow_add_new_member))
                                        ClickableText(text = annotatedString, onClick = { offset ->
                                            Timber.d("Click!")
                                        })
                                    }
                                },
                                action = {
                                    Switch(
                                        checked = adminAllowEnable,
                                        onCheckedChange = { adminAllowEnable = it })
                                },
                                textFullWeightEnable = true,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}