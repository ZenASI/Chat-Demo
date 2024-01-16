package com.chat.joycom.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.ui.commom.OtpInput
import com.chat.joycom.ui.commom.PhoneInput

@Composable
fun RegisterView() {
    val viewModel: LoginViewModel = viewModel()
    var nickName by rememberSaveable {
        mutableStateOf("")
    }

    var phone by rememberSaveable {
        mutableStateOf("")
    }


    var otp by rememberSaveable {
        mutableStateOf("111111")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            TextField(
                value = nickName,
                onValueChange = { nickName = it },
                label = { Text(text = "name", color = Color.Gray) })

            OtpInput(
                isEnable = phone.isNotEmpty(),
                otpText = otp,
                updateOtp = { otp = it },
                sentSms = {}
            )
            Button(onClick = {

            }) {
                Text(text = "Register")
            }
        }
    }
}