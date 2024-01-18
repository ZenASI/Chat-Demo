package com.chat.joycom.ui.setting.invite

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.ext.startShareIntent
import com.chat.joycom.ui.commom.IconTextH
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat

@Composable
fun InviteFriendsContent(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            InviteFriendsTopColumn()
        }
        item {
            InviteFriendsContactColumn()
        }
    }
}

@Composable
fun InviteFriendsTopColumn() {
    val context = LocalContext.current
    Column {
        IconTextH(
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_share),
                    "",
                    modifier = Modifier.size(50.dp)
                )
            },
            text = { Text(text = stringResource(id = R.string.share_link), modifier = Modifier.padding(start = 15.dp)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startShareIntent()
                }
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun InviteFriendsContactColumn(viewModel: InviteFriendsViewModel = viewModel()) {
    val contactList = remember {
        viewModel.onContactList
    }
    Column {
        Text(
            text = stringResource(id = R.string.from_contacts),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        contactList.forEach {
            IconTextH(
                icon = {
                    Image(
                        painterResource(id = R.drawable.ic_def_user),
                        "",
                        modifier = Modifier.size(50.dp)
                    )
                },
                text = {
                    Column(modifier = Modifier.padding(start = 15.dp)) {
                        val phoneUtil = PhoneNumberUtil.getInstance()
                        val phone = phoneUtil.parse("+886987654321", null)
                        Text(text = it.nickname)
                        Text(
                            text = PhoneNumberUtil.getInstance()
                                .format(phone, PhoneNumberFormat.INTERNATIONAL)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                    }
                    .padding(horizontal = 10.dp)
            )
        }
    }
}