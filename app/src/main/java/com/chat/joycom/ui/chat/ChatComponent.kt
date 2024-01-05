package com.chat.joycom.ui.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.TypedValueCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chat.joycom.R
import com.chat.joycom.ext.toSendTimeFormat
import com.chat.joycom.ext.toTopTimeFormat
import com.chat.joycom.model.Message
import com.chat.joycom.network.UrlPath
import com.chat.joycom.network.UrlPath.getFileFullUrl

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
)
@Composable
fun ChatInput(
    isGroup: Boolean,
    modifier: Modifier = Modifier,
    onMessage: ((message: Message) -> Unit)? = null,
    id: Long?,
) {
    val context = LocalContext.current
    val res = LocalContext.current.resources
    val imeState = WindowInsets.isImeVisible // for keyboard show/hide bool
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val keyboardController = LocalSoftwareKeyboardController.current // show/hide keyboard
    val focusRequester = remember { FocusRequester() }
    var inputText by rememberSaveable {
        mutableStateOf("")
    }
    val rightIcon by remember {
        derivedStateOf {
            if (inputText.isEmpty()) R.drawable.ic_mic else R.drawable.ic_send
        }
    }
    val imeHeightDP = rememberSaveable {
        mutableFloatStateOf(0f) // 紀錄鍵盤最大值
    }
    LaunchedEffect(imeBottom) {
        if (imeState)
            imeHeightDP.floatValue = maxOf(imeHeightDP.floatValue, TypedValueCompat.pxToDp(imeBottom.toFloat(), res.displayMetrics))
    }

    val bottomHeight = animateDpAsState(
        targetValue = if (imeState) imeHeightDP.floatValue.dp else 0.dp,
        label = "",
    )

    val scrollState = rememberScrollState(0)
    LaunchedEffect(scrollState.maxValue) {
         scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = modifier
//            .imePadding()
            .wrapContentHeight()
            .padding(horizontal = 5.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray, RoundedCornerShape(40.dp))
                    .padding(horizontal = 1.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_emoji),
                    "",
                    Modifier
                        .size(56.dp)
                        .scale(.6f)
                        .clickable {
                            if (!imeState) {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                        }
                )
                // ref:https://github.com/JetBrains/compose-multiplatform/issues/202
                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .heightIn(min = 56.dp, max = 112.dp)
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .verticalScroll(scrollState),
                    decorationBox = @Composable { innerTextField ->
                        // places leading icon, text field with label and placeholder, trailing icon
                        TextFieldDefaults.DecorationBox(
                            value = inputText,
                            innerTextField = innerTextField,
                            placeholder = { Text(text = stringResource(id = R.string.send_msg))},
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = remember { MutableInteractionSource() },
                            contentPadding = PaddingValues(0.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                        )
                    },
                )
                Icon(
                    painterResource(id = R.drawable.ic_file),
                    "",
                    Modifier
                        .size(56.dp)
                        .rotate(-45f)
                        .scale(.6f)
                )
                Icon(
                    painterResource(id = R.drawable.ic_camera),
                    "",
                    Modifier
                        .size(56.dp)
                        .scale(.6f)
                )
            }
            Icon(
                painterResource(id = rightIcon),
                "",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Green, CircleShape)
                    .clickable {

                    }
                    .padding(10.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomHeight.value)
        ) {
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelfMsg(message: Message, modifier: Modifier = Modifier) {
    val viewModel: ChatViewModel = viewModel()
    val memberInfo = viewModel.memberInfo.collectAsState(initial = null)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (message.msgType) {
            -1 -> {
                // top time
                Text(
                    text = message.sendTime.toTopTimeFormat(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .padding(top = 3.dp)
                        .align(Alignment.End)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    bottomStart = 8.dp,
                                    topEnd = 8.dp
                                )
                            )
                            .background(Color.Green.copy(alpha = .5f))
                            .align(Alignment.CenterEnd)
                    ) {
                        // replay
                        // TODO: replay layout
                        // content
                        Text(
                            text = message.content,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 3.dp)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {}
                                )
                        )
                        // image or not
                        // TODO: image from user

                        // time and read
                        Row(
                            modifier = Modifier.align(Alignment.End),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = message.sendTime.toSendTimeFormat(),
                                modifier = Modifier.wrapContentWidth(),
                                maxLines = 1
                            )
                            Icon(Icons.Filled.Check, "")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OtherMsg(message: Message, modifier: Modifier = Modifier) {
    val viewModel: ChatViewModel = viewModel()
    val fromUserId = remember { mutableLongStateOf(message.fromUserId) }

    val groupContactList = viewModel.groupContactList.collectAsState(initial = mutableListOf())
    val groupContact = remember(fromUserId, groupContactList) {
        derivedStateOf {
            groupContactList.value.firstOrNull { it.userId == fromUserId.longValue }
        }
    }
    val contact = viewModel.contactInfo.collectAsState()

    val nickName = remember(groupContact, contact) {
        derivedStateOf {
            if (message.isGroup) groupContact.value?.nickname else contact.value?.nickname
        }
    }
    val phone = remember(groupContact, contact) {
        derivedStateOf {
            if (message.isGroup) "" else "+" + contact.value?.countryCode + contact.value?.phoneNumber
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (message.msgType) {
            -1 -> {
                // top time
                Text(
                    text = message.sendTime.toTopTimeFormat(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
            }

            else -> {
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .padding(top = 3.dp)
                        .align(Alignment.Start),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (message.showIcon) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(UrlPath.GET_FILE.getFileFullUrl() + if (message.isGroup) groupContact.value?.avatar else contact.value?.avatar)
                                .crossfade(true).build(),
                            contentDescription = "",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .clip(CircleShape)
                                .align(Alignment.Top)
                                .clickable {
                                    // TODO: show user info card
                                },
                            placeholder = painterResource(id = R.drawable.ic_def_user),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.ic_def_user)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(50.dp))
                    }
                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomEnd = 8.dp
                                )
                            )
                            .background(Color.DarkGray.copy(alpha = .5f))
                            .fillMaxWidth(.9f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // reply
                        // TODO: reply layout
                        // name and phone
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = nickName.value ?: "",
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = phone.value,
                                maxLines = 1,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                            )
                        }
                        // image or not
                        // TODO: image from user
                        // content
                        Text(
                            text = message.content,
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .align(Alignment.Start)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {}
                                )
                        )
                        // time
                        Text(
                            text = message.sendTime.toSendTimeFormat(),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 3.dp)
                        )
                    }
                }
            }
        }
    }
}