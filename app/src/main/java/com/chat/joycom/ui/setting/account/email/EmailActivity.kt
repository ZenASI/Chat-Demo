package com.chat.joycom.ui.setting.account.email

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.joycom.R
import com.chat.joycom.ext.isValidEmail
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.DefaultInput
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.android.awaitFrame

@AndroidEntryPoint
class EmailActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, EmailActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    awaitFrame()
                    focusRequester.requestFocus()
                }
                Scaffold(
                    topBar = {
                        JoyComAppBar(title = { Text(text = stringResource(id = R.string.email_address)) })
                    }
                ) { paddingValues ->
                    var emailInput by remember {
                        mutableStateOf("")
                    }
                    Column(
                        Modifier
                            .padding(paddingValues)
                            .padding(horizontal = 20.dp)
                            .imePadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(text = stringResource(id = R.string.add_new_email_address), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(text = stringResource(id = R.string.add_new_email_address_desc))
                        DefaultInput(
                            inputText = emailInput,
                            onValueChange = { emailInput = it },
                            hint = R.string.email_address,
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 22.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            enableBottomLine = true
                        )
                        Text(
                            text = stringResource(id = R.string.hint_add_new_email_address),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = emailInput.isValidEmail()
                        ) {
                            Text(text = stringResource(id = R.string.next_step))
                        }
                    }
                }
            }
        }
    }
}