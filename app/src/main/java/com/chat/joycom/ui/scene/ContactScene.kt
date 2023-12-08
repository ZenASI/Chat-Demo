package com.chat.joycom.ui.scene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.MainActivityViewModel

@Composable
fun ContactScene() {

    val viewModel: MainActivityViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Contact", textAlign = TextAlign.Center, modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}