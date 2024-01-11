package com.chat.joycom.ui.setting.user.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.chat.joycom.ui.commom.DropdownColumn
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.TopBarIcon
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

    @OptIn(
        ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isExpanded by remember {
                mutableStateOf(false)
            }
            JoyComTheme {
                Scaffold(topBar = {
                    JoyComAppBar(
                        title = { Text(text = stringResource(id = R.string.about_me)) },
                        acton = {
                            Box {
                                TopBarIcon(R.drawable.ic_more_vert, onClick = { isExpanded = true })
                                DropdownColumn(
                                    showState = isExpanded,
                                    onDismissRequest = { isExpanded = false },
                                    itemList = listOf(R.string.delete_all),
                                    itemClick = {
                                        isExpanded = false
                                    }
                                )
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
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = { viewModel.upDateSelect(about) },
                                            onLongClick = { itemIsExpanded = true }
                                        )
                                )
                                DropdownColumn(
                                    showState = itemIsExpanded,
                                    onDismissRequest = { itemIsExpanded = false  },
                                    itemList = listOf(R.string.delete),
                                    itemClick = {
                                        itemIsExpanded = false
                                        viewModel.deleteAbout(about)
                                    },
                                    offset = DpOffset(offset.value.x.dp, 0.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}