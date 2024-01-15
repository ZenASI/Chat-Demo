package com.chat.joycom.ui.setting.invite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.JoyComTopSearchBar
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteFriendsActivity : BaseActivity() {

    private val viewModel by viewModels<InviteFriendsViewModel>()

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, InviteFriendsActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Scaffold(
                    topBar = {
                        if (viewModel.showSearchBool) {
                            JoyComTopSearchBar(
                                clickBack = {
                                    viewModel.showSearchBool =
                                        viewModel.showSearchBool.not()
                                },
                                updateText = {
                                    viewModel.searchInputText.value = it
                                },
                            )
                        } else {
                            JoyComAppBar(
                                title = { Text(text = stringResource(id = R.string.invite_friends)) },
                                acton = {
                                    TopBarIcon(drawableId = R.drawable.ic_search, onClick = {
                                        viewModel.showSearchBool =
                                            viewModel.showSearchBool.not()
                                    })
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    InviteFriendsContent(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}