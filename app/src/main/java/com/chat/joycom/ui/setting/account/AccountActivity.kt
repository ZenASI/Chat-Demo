package com.chat.joycom.ui.setting.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.setting.account.email.EmailActivity
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, AccountActivity::class.java)
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
                            title = { Text(text = stringResource(id = R.string.account)) }
                        )
                    }
                ) { paddingValues ->
                    val optionList = listOf(
                        Pair(R.string.safe_notify, R.drawable.ic_safe_notify),
                        Pair(R.string.email_address, R.drawable.ic_email),
                        Pair(R.string.two_step_verify, R.drawable.ic_two_step_verify),
                        Pair(R.string.modify_phone, R.drawable.ic_modify_phone),
                        Pair(R.string.require_account_info, R.drawable.ic_safe_notify),
                        Pair(R.string.add_new_account, R.drawable.ic_add_person),
                        Pair(R.string.delete_account, R.drawable.ic_delete),
                    )
                    val context = LocalContext.current
                    Column(Modifier.padding(paddingValues)) {
                        optionList.forEachIndexed { index, item ->
                            IconTextH(
                                icon = {
                                    Icon(
                                        painterResource(id = item.second),
                                        "",
                                        modifier = Modifier.size(40.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(id = item.first),
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                },
                                spaceWeightEnable = Pair(false, true),
                                modifier = Modifier
                                    .clickable {
                                        when (index){
                                            0 -> SafetyNotifyActivity.start(context)
                                            1 -> EmailActivity.start(context)
                                            2 -> TwoStepVerifyActivity.start(context)
                                            3 -> ModifyPhoneNumActivity.start(context)
                                            else -> {}
                                        }
                                    }
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
