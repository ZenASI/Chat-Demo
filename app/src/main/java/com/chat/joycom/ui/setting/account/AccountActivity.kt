package com.chat.joycom.ui.setting.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme

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

    @OptIn(ExperimentalMaterial3Api::class)
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
                        R.string.save_notify,
                        R.string.email_address,
                        R.string.two_step_verify,
                        R.string.modify_phone,
                        R.string.require_account_info,
                        R.string.add_new_account,
                        R.string.delete_account
                    )
                    Column(Modifier.padding(paddingValues)) {
                        optionList.forEachIndexed { index, id ->
                            IconTextH(
                                icon = { Icon(painterResource(id = R.drawable.ic_def_user), "") },
                                text = { Text(text = stringResource(id = id)) },
                                spaceWeightEnable = Pair(false, true)
                            )
                        }
                    }
                }
            }
        }
    }
}