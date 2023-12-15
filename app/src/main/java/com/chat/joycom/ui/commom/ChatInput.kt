package com.chat.joycom.ui.commom

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.model.Message

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    onMessage: ((message: Message) -> Unit)? = null,
) {
    val context = LocalContext.current
    val imeState = WindowInsets.isImeVisible // for keyboard show/hide bool

    val keyboardController = LocalSoftwareKeyboardController.current // show/hide keyboard
    var inputText by rememberSaveable {
        mutableStateOf("")
    }
    var leftBtnState by remember {
        mutableStateOf(LeftBtnState.MicroPhone)
    }
    val rightBtnState =
        (if (inputText.isEmpty()) RightBtnState.More else RightBtnState.Send)
    var toolBarVisibleState by remember {
        mutableStateOf(false) // for more tool bar visible or not
    }

    BackHandler(enabled = toolBarVisibleState) {
        toolBarVisibleState = false
//        keyboardController?.hide()
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        LazyColumn() {
            // TODO: for replay or @at group
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
        ) {
            IconButton(onClick = {
                leftBtnState = if (leftBtnState == LeftBtnState.KeyBoard) {
                    LeftBtnState.MicroPhone
                } else {
                    LeftBtnState.KeyBoard
                }
            }) {
                Crossfade(targetState = leftBtnState, label = "") {
                    when (it) {
                        LeftBtnState.MicroPhone -> Image(
                            painterResource(id = R.drawable.ic_mic),
                            "",
                            modifier = Modifier.size(40.dp)
                        )

                        LeftBtnState.KeyBoard -> Image(
                            painterResource(id = R.drawable.ic_keyboard),
                            "",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
            Crossfade(
                targetState = leftBtnState,
                label = "",
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 56.dp)
            ) {
                when (it) {
                    LeftBtnState.KeyBoard -> {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {}
                            )
                        ) {
                            Text(
                                text = "press to record",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    LeftBtnState.MicroPhone -> {
                        TextField(
                            value = inputText,
                            onValueChange = { value -> inputText = value },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            IconButton(
                onClick = {
                    if (rightBtnState == RightBtnState.Send) {
                        keyboardController?.hide()
                        toolBarVisibleState = false
//                        onMessage?.invoke(Message()) // sent
                        inputText = "" // clear
                    } else {
                        keyboardController?.hide()
                        toolBarVisibleState = toolBarVisibleState.not()
                    }
                }
            ) {
                Crossfade(targetState = rightBtnState, label = "") {
                    when (it) {
                        RightBtnState.Send ->
                            Image(
                                Icons.Filled.Send,
                                "",
                                modifier = Modifier.size(40.dp)
                            )

                        RightBtnState.More ->
                            Image(
                                Icons.Rounded.Add,
                                "",
                                modifier = Modifier.size(40.dp)
                            )
                    }
                }
            }
        }
        AnimatedContent(targetState = toolBarVisibleState, label = "") {
            if (it) {
                val featureList = ToolsFeature.values().toList()
                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                        //When the user has selected a photo, its URI is returned here
                    }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                ) {
                    featureList.forEach { item ->
                        item(key = item.ordinal) {
                            IconTextV(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clickable {
                                        when (item) {
                                            ToolsFeature.ImagePick -> {
                                                launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                                            }

                                            ToolsFeature.TakePicture -> {

                                            }

                                            ToolsFeature.FilePick -> {

                                            }
                                        }
                                    },
                                text = { Text(text = item.featureName) },
                                icon = { Image(painterResource(id = item.resId), "")})
                        }
                    }
                }
            }
        }
    }
}

enum class LeftBtnState {
    KeyBoard,
    MicroPhone
}

enum class RightBtnState {
    More,
    Send
}

enum class ToolsFeature(
    val featureName: String,
    @DrawableRes val resId: Int,
) {
    ImagePick(featureName = "image", resId = R.drawable.ic_image),
    TakePicture(featureName = "shot", resId = R.drawable.ic_camera),
    FilePick(featureName = "file", resId = R.drawable.ic_file),
}

enum class TopFeature {
    Nothing,
    Replay,
    Tag
}