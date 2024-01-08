package com.chat.joycom.ui.main.contacts.add.group

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.model.Member
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.GroupIconSelectSheet
import com.chat.joycom.ui.commom.IconTextH
import com.chat.joycom.ui.commom.IconTextV
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.commom.LimitedTimeAlert
import com.chat.joycom.ui.theme.JoyComFabTheme
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint

const val Add_GROUP_LIST = "Add_GROUP_LIST"

@AndroidEntryPoint
class AddGroupActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
            list: ArrayList<Member>,
        ) {
            context.startActivity(
                Intent(context, AddGroupActivity::class.java).apply {
                    putExtra(Add_GROUP_LIST, list)
                }
            )
        }
    }

    private val list by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Add_GROUP_LIST, Member::class.java)
        } else {
            intent.getParcelableArrayListExtra<Member>(Add_GROUP_LIST)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Surface {
                    Scaffold(
                        topBar = {
                            JoyComAppBar(
                                title = { Text(text = stringResource(id = R.string.new_group)) }
                            )
                        },
                        floatingActionButton = {
                            JoyComFabTheme {
                                FloatingActionButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.Check, "")
                                }
                            }
                        }
                    ) { paddingValues ->
                        val context = LocalContext.current
                        var groupName by remember {
                            mutableStateOf("")
                        }
                        var timeLimitedShowState by remember {
                            mutableStateOf(false)
                        }
                        var selectTimeId by remember {
                            mutableIntStateOf(R.string.close)
                        }
                        var groupIconShowState by remember {
                            mutableStateOf(false)
                        }
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(horizontal = 10.dp),
                        ) {
                            IconTextH(
                                icon = {
                                    Image(
                                        painterResource(id = R.drawable.ic_camera),
                                        "",
                                        modifier = Modifier
                                            .clickable {
                                                groupIconShowState = true
                                            }
                                            .size(50.dp)
                                            .background(Color.Gray, CircleShape)
                                            .padding(10.dp)
                                    )
                                },
                                text = {
                                    TextField(
                                        value = groupName,
                                        onValueChange = { groupName = it },
                                        label = { Text(text = stringResource(id = R.string.hint_group_name)) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                action = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_emoji),
                                        "",
                                    )
                                },
                                textFullWeightEnable = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            )
                            IconTextH(
                                text = {
                                    Column {
                                        Text(text = stringResource(id = R.string.limited_time_msg))
                                        Text(text = stringResource(id = selectTimeId))
                                    }
                                },
                                action = {
                                    Icon(painterResource(id = R.drawable.ic_time), "")
                                },
                                modifier = Modifier
                                    .clickable {
                                        timeLimitedShowState = true
                                    }
                                    .fillMaxWidth()
                                    .height(70.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            IconTextH(
                                text = {
                                    Text(text = stringResource(id = R.string.group_permission))
                                },
                                action = {
                                    Icon(painterResource(id = R.drawable.ic_setting), "")
                                },
                                modifier = Modifier
                                    .clickable {
                                        GroupPermissionActivity.start(context)
                                    }
                                    .fillMaxWidth()
                                    .height(70.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            Text(
                                text = stringResource(id = R.string.group_member, list?.size ?: 0),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .wrapContentHeight(Alignment.CenterVertically),
                                textAlign = TextAlign.Start
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                list?.forEach {
                                    item {
                                        IconTextV(
                                            icon = {
                                                Image(
                                                    painterResource(id = R.drawable.ic_def_user),
                                                    "",
                                                    modifier = Modifier.size(50.dp)
                                                )
                                            },
                                            text = { Text(text = it.nickname) },
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        )
                                    }
                                }
                            }
                        }
                        if (timeLimitedShowState) {
                            LimitedTimeAlert(
                                showState = { timeLimitedShowState = it },
                                selectCallback = { selectTimeId = it })
                        }
                        if (groupIconShowState) {
                            GroupIconSelectSheet(showState = { groupIconShowState = it })
                        }
                    }
                }
            }
        }
    }
}