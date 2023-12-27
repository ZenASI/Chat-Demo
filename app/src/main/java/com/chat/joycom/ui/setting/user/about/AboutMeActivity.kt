package com.chat.joycom.ui.setting.user.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val DEFAULT_ABOUT_LIST = "DEFAULT_ABOUT_LIST"

@AndroidEntryPoint
class AboutMeActivity : BaseActivity() {
    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, AboutMeActivity::class.java)
            )
        }
    }

    private val viewModel by viewModels<AboutMeViewModel>(extrasProducer = {
        val stringArray = resources.getStringArray(R.array.about_list)
        Timber.d("$stringArray")
        // pass to viewModel params
        MutableCreationExtras(defaultViewModelCreationExtras).apply {
            set(
                DEFAULT_ARGS_KEY, bundleOf(
                    DEFAULT_ABOUT_LIST to stringArray
                )
            )
        }
    })

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isExpanded by remember {
                mutableStateOf(false)
            }
            JoyComTheme {
                Surface {
                    Scaffold(topBar = {
                        JoyComAppBar(
                            showBack = true,
                            title = { Text(text = stringResource(id = R.string.about_me)) },
                            acton = {
                                Box() {
                                    Icon(Icons.Filled.MoreVert, "", modifier = Modifier.clickable {
                                        isExpanded = true
                                    })
                                    DropdownMenu(
                                        expanded = isExpanded,
                                        onDismissRequest = { isExpanded = false }) {
                                        DropdownMenuItem(
                                            text = { Text(text = stringResource(id = R.string.delete_all)) },
                                            onClick = { isExpanded = false })
                                    }
                                }

                            }
                        )
                    }) { paddingValues ->

                        val itemList =
                            viewModel.aboutList.collectAsState(initial = emptyList()).value
                        val offset = remember {
                            mutableStateOf(Offset.Zero)
                        }
                        Column(modifier = Modifier.padding(paddingValues)) {
                            itemList.forEach { about ->
                                var itemIsExpanded by remember {
                                    mutableStateOf(false)
                                }
                                Box(modifier = Modifier.pointerInteropFilter {
                                    offset.value = Offset(it.x, it.y)
                                    false
                                }) {
                                    IconTextH(
                                        text = {
                                            Text(
                                                about.aboutText,
                                                modifier = Modifier
                                            )
                                        },
                                        action = {
                                            if (about.isSelect) {
                                                Icon(
                                                    Icons.Filled.Check,
                                                    "",
                                                    modifier = Modifier.size(50.dp)
                                                )
                                            }
                                        },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .combinedClickable(
                                                onClick = { viewModel.upDateSelect(about) },
                                                onLongClick = { itemIsExpanded = true }
                                            )
                                    )
                                    DropdownMenu(
                                        expanded = itemIsExpanded,
                                        offset = DpOffset(offset.value.x.dp, 0.dp),
                                        onDismissRequest = { itemIsExpanded = false }) {
                                        DropdownMenuItem(
                                            text = { Text(text = stringResource(id = R.string.delete)) },
                                            onClick = {
                                                itemIsExpanded = false
                                                viewModel.deleteAbout(about)
                                            })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}