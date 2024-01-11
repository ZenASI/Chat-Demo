package com.chat.joycom.ui.setting.account

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SafetyNotifyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Scaffold() { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                    }
                }
            }
        }
    }
}