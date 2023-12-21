package com.chat.joycom.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.Message
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl
import com.chat.joycom.ui.commom.ChatInput
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.OtherMsg
import com.chat.joycom.ui.commom.SelfMsg
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

const val CONTACT_INFO = "CONTACT_INFO"
const val GROUP_INFO = "GROUP_INFO"
const val IS_GROUP = "IS_GROUP"

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class ChatActivity : ComponentActivity() {

    private val contact by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CONTACT_INFO, Contact::class.java)
        } else {
            intent.getParcelableExtra(CONTACT_INFO) as Contact?
        }
    }

    private val group by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(GROUP_INFO, Group::class.java)
        } else {
            intent.getParcelableExtra(GROUP_INFO) as Group?
        }
    }

    private val isGroupBool by lazy {
        intent.getBooleanExtra(IS_GROUP, false)
    }

    private val viewModel by viewModels<ChatViewModel>(extrasProducer = {
        // pass to viewModel params
        MutableCreationExtras(defaultViewModelCreationExtras).apply {
            set(
                DEFAULT_ARGS_KEY, bundleOf(
                    IS_GROUP to isGroupBool,
                    CONTACT_INFO to contact,
                    GROUP_INFO to group
                )
            )
        }
    })

    companion object {
        fun start(
            context: Context,
            contact: Contact? = null,
            group: Group? = null,
            isGroup: Boolean
        ) {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra(CONTACT_INFO, contact)
                putExtra(GROUP_INFO, group)
                putExtra(IS_GROUP, isGroup)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = if (isGroupBool) group?.groupName else contact?.nickname
        setContent {
            JoyComTheme {
                Surface {
                    Scaffold(
                        topBar = {
                            JoyComAppBar(
                                showBack = true,
                                title = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(UrlPath.GET_FILE.getFileFullUrl() + if (isGroupBool) group?.avatar else contact?.avatar)
                                                .crossfade(true).build(),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .width(50.dp)
                                                .height(50.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    // TODO: show user info card
                                                },
                                            placeholder = painterResource(id = if (isGroupBool) R.drawable.ic_def_group else R.drawable.ic_def_user),
                                            contentScale = ContentScale.Crop,
                                            error = painterResource(id = if (isGroupBool) R.drawable.ic_def_group else R.drawable.ic_def_user)
                                        )
                                        Text(title ?: "")
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            ChatInput(
                                isGroup = isGroupBool,
                                id = if (isGroupBool) group?.groupId else contact?.userId,
                                onMessage = { viewModel.sentMessage(it) }
                            )
                        }
                    ) { paddingValues ->
                        val memberInfo = viewModel.memberInfo.collectAsState(initial = null).value
                        val messageList =
                            viewModel.messageList.collectAsState(initial = mutableListOf()).value

                        if (memberInfo != null) {
                            val lazyState = rememberLazyListState()
                            LazyColumn(
                                modifier = Modifier.padding(paddingValues),
                                state = lazyState,
                                reverseLayout = false
                            ) {
                                items(
                                    items = messageList,
                                    key = { item: Message -> item.id },
                                    contentType = { item: Message -> item.fromUserId }
                                ) { item ->
                                    when (item.fromUserId) {
                                        memberInfo.userId -> SelfMsg(message = item)
                                        else -> OtherMsg(message = item)
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