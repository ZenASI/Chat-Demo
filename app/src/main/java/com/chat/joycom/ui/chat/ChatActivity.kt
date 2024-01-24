package com.chat.joycom.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.paging.compose.collectAsLazyPagingItems
import com.chat.joycom.R
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.SimpleUrlImage
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

const val CONTACT_INFO = "CONTACT_INFO"
const val GROUP_INFO = "GROUP_INFO"
const val IS_GROUP = "IS_GROUP"

@AndroidEntryPoint
class ChatActivity : BaseActivity() {

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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JoyComTheme {
                Scaffold(
                    topBar = {
                        JoyComAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    SimpleUrlImage(
                                        url = "https://picsum.photos/200",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape),
                                        error = painterResource(id = if (isGroupBool) R.drawable.ic_def_group else R.drawable.ic_def_user),
                                        placeholder = painterResource(id = if (isGroupBool) R.drawable.ic_def_group else R.drawable.ic_def_user)
                                    )
                                    Text(title ?: "")
                                }
                            },
                            acton = { ChatTopBarAction(isGroupBool) }
                        )
                    },
                    bottomBar = {
                        ChatInput(
                            isGroup = isGroupBool,
                            id = if (isGroupBool) group?.groupId else contact?.userId,
                            modifier = Modifier
                        )
                    },
                    modifier = Modifier.navigationBarsPadding()
                ) { paddingValues ->
                    val memberInfo = viewModel.memberInfo.collectAsState(initial = null).value
//                    val pagingList = viewModel.pagingMessage.collectAsLazyPagingItems()
                    val protoTypeList = remember {
                        viewModel.protoTypeMessage
                    }
                    val lazyState = rememberLazyListState()

                    if (memberInfo != null) {
                        LazyColumn(
                            modifier = Modifier.padding(paddingValues),
                            state = lazyState,
                            reverseLayout = true
                        ) {
                            items(protoTypeList) {
                                when (it.fromUserId) {
                                    // 目前-1表示自發訊息
                                    -1L -> SelfMsg(message = it)
                                    else -> OtherMsg(message = it)
                                }
                            }
                            // for paging-msg
//                            items(count = pagingList.itemCount) { pos ->
//                                val item = pagingList[pos]
//                                item?.let {
//                                    when (item.fromUserId) {
//                                        memberInfo.userId -> SelfMsg(message = item)
//                                        else -> OtherMsg(message = item)
//                                    }
//                                }
//                            }
                        }
                    }
                }
            }
        }
    }
}