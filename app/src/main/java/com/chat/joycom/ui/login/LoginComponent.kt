package com.chat.joycom.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.commom.OtpInput
import com.chat.joycom.ui.commom.PhoneInput
import com.utkuglsvn.countrycodepicker.libData.utils.getLibCountries

@Composable
fun LoginView() {

    val viewModel: LoginViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState(initial = UiEvent.EmptyEvent).value

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var phone by rememberSaveable {
                mutableStateOf("")
            }
            var otp by rememberSaveable {
                mutableStateOf("111111")
            }
            var cc by remember {
                mutableStateOf(getLibCountries().single { it.countryCode == "tw" })
            }
            PhoneInput(
                inputText = phone,
                updateText = { phone = it },
                pickCountry = { cc = it },
                defaultCountry = cc,
            )

            OtpInput(
                isEnable = phone.isNotEmpty(),
                otpText = otp,
                updateOtp = { otp = it },
                sentSms = { viewModel.sendSms(cc.countryPhoneCode, phone) })

            Button(onClick = {
                viewModel.goLogin(cc.countryPhoneCode, phone, otp)
            }) {
                Text(text = "Login")
            }
        }
    }
}