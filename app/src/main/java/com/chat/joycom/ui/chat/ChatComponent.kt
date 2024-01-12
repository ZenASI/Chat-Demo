package com.chat.joycom.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
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
import com.chat.joycom.ui.commom.DefaultInput
import com.chat.joycom.ui.commom.IconTextV
import com.chat.joycom.ui.commom.TopBarIcon
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.max

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun ChatInput(
    isGroup: Boolean,
    modifier: Modifier = Modifier,
    onMessage: ((message: Message) -> Unit)? = null,
    id: Long?,
) {
    var popUpShowState by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    var inputText by rememberSaveable {
        mutableStateOf("")
    }
    val sendIconType by remember {
        derivedStateOf {
            if (inputText.isEmpty()) R.drawable.ic_mic else R.drawable.ic_send
        }
    }
    var isTypeKeyBoard by remember {
        mutableStateOf(false)
    }
    val keyBoardType by remember {
        derivedStateOf {
            if (isTypeKeyBoard) R.drawable.ic_keyboard else R.drawable.ic_emoji
        }
    }
    val scrollState = rememberScrollState(0)
    LaunchedEffect(scrollState.maxValue) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val res = LocalContext.current.resources
    val config = LocalConfiguration.current
    val imeState = WindowInsets.isImeVisible // for keyboard show/hide bool
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val keyboardController = LocalSoftwareKeyboardController.current // show/hide keyboard

    var limitReduceCount by remember {
        mutableIntStateOf(0)
    }

    var defaultHeightDp by remember {
        mutableFloatStateOf((config.screenHeightDp / 3).toFloat())
    }

    var keyBoardHeightDp by remember {
        mutableFloatStateOf(0f)
    }

    var bottomHeightDp by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(imeBottom) {
        if (imeBottom != 0) {
            val imeBottomDp = TypedValueCompat.pxToDp(imeBottom.toFloat(), res.displayMetrics)
            keyBoardHeightDp = maxOf(keyBoardHeightDp, imeBottomDp)
            bottomHeightDp = maxOf(defaultHeightDp, imeBottomDp)
        }
    }

    BackHandler(bottomHeightDp > 0f) {
        isTypeKeyBoard = false
        bottomHeightDp = 0f
    }

    Column(
        modifier = modifier
            .wrapContentHeight()
            .padding(horizontal = 5.dp),
    ) {
        ChatPopUpMenu(popUpShowState, onDismiss = { popUpShowState = it })
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
                    painterResource(id = keyBoardType),
                    "",
                    Modifier
                        .size(56.dp)
                        .scale(.6f)
                        .clip(CircleShape)
                        .clickable {
                            if (isTypeKeyBoard) {
                                keyboardController?.show()
                                focusRequester.requestFocus()

                            } else {
                                if (bottomHeightDp == 0f) {
                                    bottomHeightDp = defaultHeightDp
                                }
                                keyboardController?.hide()
                            }
                            isTypeKeyBoard = isTypeKeyBoard.not()
                        }
                )
                DefaultInput(
                    inputText = inputText,
                    onValueChange = { inputText = it },
                    hint = R.string.send_msg,
                    modifier = Modifier
                        .heightIn(min = 56.dp, max = 112.dp)
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .verticalScroll(scrollState),
                )
                Icon(
                    painterResource(id = R.drawable.ic_file),
                    "",
                    Modifier
                        .size(56.dp)
                        .rotate(-45f)
                        .scale(.6f)
                        .clickable {
                            popUpShowState = true
                        }
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
                painterResource(id = sendIconType),
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
                .animateContentSize()
                .fillMaxWidth()
                .height(bottomHeightDp.dp)
        ) {
        }
    }
}

@Composable
fun ChatPopUpMenu(showState: Boolean, onDismiss: (Boolean) -> Unit) {
    val animationProgress by animateFloatAsState(
        targetValue = if (showState) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing), label = ""
    )

    val transition = updateTransition(targetState = animationProgress, label = "")
    val res = LocalContext.current.resources

    Popup(
        onDismissRequest = { onDismiss.invoke(false) },
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -TypedValueCompat.dpToPx(60f, res.displayMetrics).toInt())
    ) {
        val animatedShape by transition.animateValue(
            TwoWayConverter(
                convertToVector = { AnimationVector1D(0f) },
                convertFromVector = { GenericShape { _, _ -> } }
            ),
            label = ""
        ) { progress ->
            GenericShape { size, _ ->
                val centerH = size.width / 2f
                val multiplierW = 1.5f + size.height / size.width

                moveTo(
                    x = centerH - centerH * progress * multiplierW,
                    y = size.height,
                )

                val currentWidth = (centerH * progress * multiplierW * 2.5f)

                cubicTo(
                    x1 = centerH - centerH * progress * 1.5f,
                    y1 = size.height - currentWidth * 0.5f,
                    x2 = centerH + centerH * progress * 1.5f,
                    y2 = size.height - currentWidth * 0.5f,
                    x3 = centerH + centerH * progress * multiplierW,
                    y3 = size.height,
                )

                close()
            }
        }
        if (animationProgress != 0f) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth(.95f)
                    .graphicsLayer {
                        clip = true
                        shape = animatedShape
                    }
                    .background(Color.Gray, RoundedCornerShape(8.dp)),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 25.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                genChatPopUpItem().forEachIndexed { index, item ->
                    item {
                        IconTextV(
                            icon = {
                                Icon(
                                    painterResource(id = item.second),
                                    "",
                                    modifier = Modifier.size(50.dp)
                                )
                            },
                            text = { Text(text = stringResource(id = item.first)) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                onDismiss.invoke(false)
                            }
                        )
                    }
                }
            }
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
            "Jeff"
//            if (message.isGroup) groupContact.value?.nickname else contact.value?.nickname
        }
    }
    val phone = remember(groupContact, contact) {
        derivedStateOf {
            "+886987654321"
//            if (message.isGroup) "" else "+" + contact.value?.countryCode + contact.value?.phoneNumber
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
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
                        if (message.isGroup) {
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

@Composable
fun ChatTopBarAction() {
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        TopBarIcon(R.drawable.ic_videocam, onClick = {})
        TopBarIcon(R.drawable.ic_phone, onClick = {})
        TopBarIcon(R.drawable.ic_more_vert, onClick = {})
    }
}

private fun genChatPopUpItem(): List<Pair<Int, Int>> =
    listOf(
        Pair(R.string.file, R.drawable.ic_file),
        Pair(R.string.camera, R.drawable.ic_camera),
        Pair(R.string.gallery, R.drawable.ic_image),
        Pair(R.string.audio, R.drawable.ic_head_phone),
        Pair(R.string.location, R.drawable.ic_location),
        Pair(R.string.contacts, R.drawable.ic_contacts),
        Pair(R.string.vote, R.drawable.ic_vote),
    )