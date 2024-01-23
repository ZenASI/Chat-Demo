package com.chat.joycom.ui.setting.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme

class ModifyPhoneNumActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, ModifyPhoneNumActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Scaffold(
                    topBar = {
                        JoyComAppBar(title = { Text(text = stringResource(id = R.string.modify_phone)) })
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.size(20.dp))
                        Icon(
                            painterResource(id = R.drawable.ic_sim_card),
                            "",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.modify_phone_desc),
                            fontWeight = FontWeight.Bold,
                        )
                        Text(text = stringResource(id = R.string.modify_phone_sub_desc1))
                        Text(text = stringResource(id = R.string.modify_phone_sub_desc2))
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {}
                        ) {
                            Text(text = stringResource(id = R.string.next_step))
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
