package com.chat.joycom.ui.main.contacts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.util.TypedValueCompat
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.CirclePath
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.TopBarContactSearch
import com.chat.joycom.ui.theme.JoyComTheme

class ContactsActivity : BaseActivity() {

    private val viewModel by viewModels<ContactsViewModel>()

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, ContactsActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Scaffold(
                    topBar = {
                        if (viewModel.showSearchBool.value) {
                            TopBarContactSearch(clickBack = {
                                viewModel.showSearchBool.value =
                                    viewModel.showSearchBool.value.not()
                            })
                        } else {
                            JoyComAppBar(
                                title = {
                                    Column {
                                        Text(text = stringResource(id = R.string.select_contact))
                                        Text(
                                            text = stringResource(id = R.string.per_contacts, 3),
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                acton = { ContactsTopBarActions() }
                            )
                        }
                    }
                ) { paddingValues ->
                    ContactsColumn(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}