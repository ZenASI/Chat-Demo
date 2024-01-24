package com.chat.joycom.ui.setting.account.modifyphone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.PhoneInput
import com.chat.joycom.ui.theme.JoyComTheme

class EnterPhoneNumActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, EnterPhoneNumActivity::class.java)
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
                    var oldCountryCode by remember {
                        mutableStateOf(TextFieldValue("886", TextRange.Zero))
                    }
                    var oldPhoneNumber by remember {
                        mutableStateOf(TextFieldValue("", TextRange.Zero))
                    }
                    var newCountryCode by remember {
                        mutableStateOf(TextFieldValue("886", TextRange.Zero))
                    }
                    var newPhoneNumber by remember {
                        mutableStateOf(TextFieldValue("", TextRange.Zero))
                    }
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .imePadding(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = stringResource(id = R.string.enter_old_phone))
                        Row {
                            PhoneInput(
                                inputText = oldCountryCode,
                                onValueChange = { oldCountryCode = it },
                                modifier = Modifier.weight(1f),
                                prefixText = "+"
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            PhoneInput(
                                inputText = oldPhoneNumber,
                                onValueChange = { oldPhoneNumber = it },
                                hint = R.string.enter_phone_hint,
                                modifier = Modifier.weight(4f),
                            )
                        }
                        Text(text = stringResource(id = R.string.enter_new_phone))
                        Row {
                            PhoneInput(
                                inputText = newCountryCode,
                                onValueChange = { newCountryCode = it },
                                modifier = Modifier.weight(1f),
                                prefixText = "+"
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            PhoneInput(
                                inputText = newPhoneNumber,
                                onValueChange = { newPhoneNumber = it },
                                hint = R.string.enter_phone_hint,
                                modifier = Modifier.weight(4f),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {}
                        ) {
                            Text(text = stringResource(id = R.string.next_step))
                        }
                    }
                }
            }
        }
    }
}
