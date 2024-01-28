package com.chat.joycom.ui.chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.util.TypedValueCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chat.joycom.R
import com.chat.joycom.ext.toSendTimeFormat
import com.chat.joycom.ext.toTopTimeFormat
import com.chat.joycom.model.Message
import com.chat.joycom.ui.commom.DefaultInput
import com.chat.joycom.ui.commom.DropdownColumn
import com.chat.joycom.ui.commom.Emoji2KeyBoard
import com.chat.joycom.ui.commom.IconTextV
import com.chat.joycom.ui.commom.InfoCardDialog
import com.chat.joycom.ui.commom.OtherBubbleShape
import com.chat.joycom.ui.commom.SelfBubbleShape
import com.chat.joycom.ui.commom.SimpleDataImage
import com.chat.joycom.ui.commom.TopBarIcon
import com.chat.joycom.ui.commom.UrlType
import timber.log.Timber

@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun ChatInput(
    isGroup: Boolean,
    modifier: Modifier = Modifier,
    id: Long?,
    viewModel: ChatViewModel = viewModel()
) {
    var popUpShowState by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    var inputText by remember {
        mutableStateOf(TextFieldValue("", TextRange.Zero))
    }
    val sendIconType by remember {
        derivedStateOf {
            if (inputText.text.isEmpty()) R.drawable.ic_mic else R.drawable.ic_send
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

    val view = LocalView.current
    val res = LocalContext.current.resources
    val config = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val navigationBarBottom = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val keyboardController = LocalSoftwareKeyboardController.current // show/hide keyboard

    val defaultHeightDp by remember {
        mutableFloatStateOf((config.screenHeightDp / 4).toFloat())
    }

    var recordHeightDp by remember {
        mutableFloatStateOf(0f)
    }

    var bottomHeightDp by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(Unit) {
        ViewCompat.setWindowInsetsAnimationCallback(view, KeyBoardAnimateCallBack())
    }

    LaunchedEffect(imeBottom) {
        val imeBottomDp = TypedValueCompat.pxToDp(imeBottom.toFloat(), res.displayMetrics)
        val navigationBarBottomDp =
            TypedValueCompat.pxToDp(navigationBarBottom.toFloat(), res.displayMetrics)
        Timber.d("LaunchedEffect imeBottomDp => $imeBottomDp, isTypeKeyBoard => $isTypeKeyBoard")
        if (imeBottom == 0) {

        } else {
            recordHeightDp = imeBottomDp - navigationBarBottomDp + 3
            bottomHeightDp = imeBottomDp - navigationBarBottomDp + 3
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
                                    bottomHeightDp = if (recordHeightDp != 0f) {
                                        recordHeightDp
                                    } else {
                                        defaultHeightDp
                                    }
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
                    .size(55.dp)
                    .background(Color(0xFF00A884), CircleShape)
                    .clickable {
                        viewModel.sentMessage(
                            Message.getFakeMsg(
                                isGroup,
                                fromUserId = -1,
                                content = inputText.text
                            )
                        )
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        bottomHeightDp = 0f
                        inputText = TextFieldValue("", TextRange.Zero)
                        isTypeKeyBoard = false
                    }
                    .padding(10.dp),
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .height(bottomHeightDp.dp)
        ) {
            Emoji2KeyBoard(onEmojiPickListener = {
                inputText =
                    TextFieldValue(inputText.text + it.emoji, TextRange(inputText.text.length + 1))
            })
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
    val res = LocalContext.current.resources
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
                            .background(Color.Green.copy(alpha = .5f), SelfBubbleShape(45f))
                            .align(Alignment.CenterEnd)
                    ) {
                        // replay
                        // TODO: replay layout
                        // content
                        Content(
                            message = message,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(
                                    start = 3.dp,
                                    end = TypedValueCompat.pxToDp(40f, res.displayMetrics).dp
                                )
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {}
                                )
                        )

                        // time and read
                        Row(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = TypedValueCompat.pxToDp(40f, res.displayMetrics).dp),

                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = message.sendTime.toSendTimeFormat(),
                                modifier = Modifier.wrapContentWidth(),
                                maxLines = 1
                            )
                            Image(
                                painterResource(id = R.drawable.ic_read_checked),
                                "",
                                modifier = Modifier.size(20.dp)
                            )
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
    var cardShowState by remember {
        mutableStateOf(false)
    }
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
        val res = LocalContext.current.resources
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
                        .padding(top = 3.dp)
                        .align(Alignment.Start),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    if (message.showIcon) {
                        SimpleDataImage(
                            data = "https://picsum.photos/200",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .align(Alignment.Top)
                                .clickable {
                                    cardShowState = true
                                },
                            placeholder = painterResource(id = R.drawable.ic_def_user),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.ic_def_user)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(30.dp))
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .background(
                                    Color.DarkGray.copy(alpha = .5f),
                                    OtherBubbleShape(45f)
                                ),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            // reply
                            // TODO: reply layout
                            // name and phone
                            if (message.isGroup) {
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = TypedValueCompat.pxToDp(
                                                40f,
                                                res.displayMetrics
                                            ).dp, end = 3.dp
                                        )
                                        .wrapContentWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = nickName.value ?: "",
                                        maxLines = 1,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .widthIn(min = 10.dp)
                                            .weight(1f)
                                    )
                                    Text(
                                        text = phone.value,
                                        maxLines = 1,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier
                                    )
                                }
                            }
                            // content
                            Content(
                                modifier = Modifier
                                    .padding(
                                        start = TypedValueCompat.pxToDp(
                                            40f,
                                            res.displayMetrics
                                        ).dp, end = 3.dp
                                    )
                                    .align(Alignment.Start)
                                    .combinedClickable(
                                        onClick = {},
                                        onLongClick = {}
                                    ),
                                message = message
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
                    Spacer(modifier = Modifier.size(30.dp))
                }
            }
        }
    }

    if (cardShowState) {
        InfoCardDialog(
            onDismissRequest = { cardShowState = it },
            title = "Jeff",
            imgUrl = "https://media.tenor.com/TTHVXG5NRGgAAAAM/subaru-dancing.gif",
            callBack = {}
        )
    }
}

@Composable
fun ChatTopBarAction(isGroupBool: Boolean) {
    var showState by remember {
        mutableStateOf(false)
    }
    var moreShowState by remember {
        mutableStateOf(false)
    }
    val itemList = if (isGroupBool) {
        listOf(
            R.string.group_info,
            R.string.group_multi_media,
        )
    } else {
        listOf(
            R.string.add_to_contact,
            R.string.media_links_and_files,
        )
    } + listOf(
        R.string.search,
        R.string.mute_notification,
        R.string.limited_time_msg,
        R.string.background_image,
        R.string.more
    )
    val itemMoreList = listOf(
        R.string.report,
        if (isGroupBool) {
            R.string.exit_group
        } else {
            R.string.blockade
        },
        R.string.clear_conversation,
        R.string.out_put_conversation,
        R.string.add_shortcut,
    )
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        TopBarIcon(R.drawable.ic_videocam, onClick = {})
        TopBarIcon(R.drawable.ic_phone, onClick = {})
        Box {
            TopBarIcon(R.drawable.ic_more_vert, onClick = { showState = true })
            DropdownColumn(
                showState = showState,
                onDismissRequest = { showState = false },
                itemList = itemList,
                itemClick = { stringResId ->
                    showState = false
                    when (stringResId) {
                        R.string.more -> {
                            moreShowState = true
                        }
                    }
                }
            )
            DropdownColumn(
                showState = moreShowState,
                onDismissRequest = { moreShowState = false },
                itemList = itemMoreList,
                itemClick = { moreShowState = false }
            )
        }
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


private fun KeyBoardAnimateCallBack() =
    object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
        override fun onProgress(
            insets: WindowInsetsCompat,
            runningAnimations: MutableList<WindowInsetsAnimationCompat>
        ): WindowInsetsCompat {
            return insets
        }

        override fun onPrepare(animation: WindowInsetsAnimationCompat) {
            super.onPrepare(animation)
            Timber.d("onPrepare")
        }

        override fun onStart(
            animation: WindowInsetsAnimationCompat,
            bounds: WindowInsetsAnimationCompat.BoundsCompat
        ): WindowInsetsAnimationCompat.BoundsCompat {
            Timber.d("onStart")
            return super.onStart(animation, bounds)
        }

        override fun onEnd(animation: WindowInsetsAnimationCompat) {
            super.onEnd(animation)
            Timber.d("onEnd")
        }
    }

@Composable
private fun Content(modifier: Modifier = Modifier, message: Message) {
    Box(modifier = modifier) {
        when (message.msgType) {
            2 -> {
                // text
                Text(text = message.content)
            }

            3 -> {
                // image
                // https://i.pinimg.com/originals/82/c4/8e/82c48eb9a933331ace3ed7e5ed172270.gif
                val configuration = LocalConfiguration.current
                SimpleDataImage(
                    data = "https://i.pinimg.com/originals/82/c4/8e/82c48eb9a933331ace3ed7e5ed172270.gif",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .width((configuration.screenWidthDp / 2).dp)
                        .clickable {

                        },
                    contentScale = ContentScale.FillWidth
                )
            }

            4 -> {
                // video
                // "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                val configuration = LocalConfiguration.current
                SimpleDataImage(
                    data = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .width((configuration.screenWidthDp / 2).dp)
                        .clickable {

                        },
                    contentScale = ContentScale.FillWidth,
                    urlType = UrlType.VideoFrame
                )
            }
        }
    }
}