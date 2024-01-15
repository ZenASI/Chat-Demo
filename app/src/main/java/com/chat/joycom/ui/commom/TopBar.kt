package com.chat.joycom.ui.commom

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.util.TypedValueCompat
import com.chat.joycom.R
import com.chat.joycom.ui.theme.JoyComTopBarSearchTheme
import com.chat.joycom.ui.theme.JoyComTopBarTheme
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

@Composable
fun TopBarIcon(@DrawableRes drawableId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Icon(
        painterResource(id = drawableId),
        "",
        modifier = modifier
            .clip(CircleShape)
            .size(30.dp)
            .clickable {
                onClick.invoke()
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoyComAppBar(
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    title: @Composable () -> Unit = {},
    acton: @Composable RowScope.() -> Unit = {},
) {
    val activity = LocalContext.current as ComponentActivity
    JoyComTopBarTheme {
        TopAppBar(
            navigationIcon = {
                if (showBack)
                    TopBarIcon(
                        R.drawable.ic_arrow_back,
                        onClick = { activity.finish() }
                    )
            },
            title = title,
            modifier = modifier,
            actions = acton,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoyComTopSearchBar(
    @StringRes hint: Int = R.string.empty,
    clickBack: () -> Unit,
    updateText: (String) -> Unit,
    enableSwitchBoard: Boolean = false,
    isMsgSearch: Boolean = false,
    chipClick: (Int?) -> Unit = {},
) {
    var isListGrid by remember {
        mutableStateOf(false)
    }
    var selectFilterPos: Int? by remember {
        mutableStateOf(null)
    }
    val searchBgColor = if (isSystemInDarkTheme()) Color(0xFF202C33) else Color.LightGray
    var searchInput by remember {
        mutableStateOf("")
    }
    var keyType by remember {
        mutableStateOf(KeyboardType.Text)
    }
    val focusRequester = remember { FocusRequester() }

    val scope = rememberCoroutineScope()
    val res = LocalContext.current.resources
    val config = LocalConfiguration.current
    val screenWidth = TypedValueCompat.dpToPx(config.screenWidthDp.toFloat(), res.displayMetrics)
    val animationOffset by remember {
        mutableStateOf(
            Offset(
                screenWidth * .95f,
                TypedValueCompat.dpToPx(56f, res.displayMetrics)
            )
        )
    }
    val revealSize = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        awaitFrame()
        revealSize.snapTo(0f)
        revealSize.animateTo(
            1f,
            tween(200)
        )
        focusRequester.requestFocus()
    }

    JoyComTopBarSearchTheme {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .graphicsLayer {
                clip = true
                shape = CirclePath(revealSize.value, animationOffset)
            }
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(searchBgColor, RoundedCornerShape(40.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.size(10.dp))
                        TopBarIcon(drawableId = R.drawable.ic_arrow_back, onClick = {
                            scope.launch {
                                revealSize.snapTo(1f)
                                revealSize.animateTo(
                                    0f,
                                    tween(200)
                                )
                                awaitFrame()
                                clickBack.invoke()
                            }
                        })
                        if (selectFilterPos != null) {
                            val item = queryChipItem(selectFilterPos!!)
                            SelectChip(
                                onClick = {},
                                label = { Text(text = stringResource(id = item.second)) },
                                leadingIcon = {
                                    Icon(painterResource(id = item.first), "")
                                }
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                        DefaultInput(
                            inputText = searchInput,
                            onValueChange = {
                                searchInput = it
                                updateText.invoke(it)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester),
                            hint = hint,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyType),
                            singleLine = true
                        )
                        if (selectFilterPos != null) {
                            FilterListState(
                                isListGrid = isListGrid,
                                listTypeChange = { isListGrid = it },
                                onCancel = {
                                    selectFilterPos = null
                                    chipClick.invoke(null)
                                }
                            )
                        }
                        if (enableSwitchBoard) {
                            KeyBoardTypeSwitcher(keyType = keyType, typeChange = { keyType = it })
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                },
            )
            if (isMsgSearch && selectFilterPos == null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    MsgChipList.forEachIndexed { index, pairItem ->
                        item {
                            BasicChip(
                                onClick = {
                                    selectFilterPos = index
                                    chipClick.invoke(index)
                                },
                                label = { Text(text = stringResource(id = pairItem.second)) },
                                leadingIcon = {
                                    Icon(painterResource(id = pairItem.first), "")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterListState(
    isListGrid: Boolean,
    listTypeChange: (Boolean) -> Unit,
    onCancel: () -> Unit
) {
    TopBarIcon(
        if (isListGrid) R.drawable.ic_grid else R.drawable.ic_list,
        onClick = {
            listTypeChange.invoke(isListGrid.not())
        })
    Spacer(modifier = Modifier.size(10.dp))
    TopBarIcon(
        R.drawable.ic_add,
        modifier = Modifier.rotate(45f),
        onClick = { onCancel.invoke() })
}

@Composable
private fun KeyBoardTypeSwitcher(keyType: KeyboardType, typeChange: (KeyboardType) -> Unit) {
    TopBarIcon(
        drawableId = if (keyType == KeyboardType.Text) R.drawable.ic_key_number else R.drawable.ic_keyboard,
        onClick = {
            val result = if (keyType == KeyboardType.Text) {
                KeyboardType.Number
            } else {
                KeyboardType.Text
            }
            typeChange.invoke(result)
        })
}

private val MsgChipList =
    listOf(
        Pair(R.drawable.ic_chat, R.string.unread),
        Pair(R.drawable.ic_image, R.string.photo),
        Pair(R.drawable.ic_videocam, R.string.video),
        Pair(R.drawable.ic_link, R.string.link),
        Pair(R.drawable.ic_gif, R.string.gif),
        Pair(R.drawable.ic_head_phone, R.string.audio),
        Pair(R.drawable.ic_file, R.string.file),
        Pair(R.drawable.ic_vote, R.string.vote),
    )

private fun queryChipItem(pos: Int): Pair<Int, Int> {
    return MsgChipList[pos]
}

class CirclePath(
    private val progress: Float,
    private val origin: Offset = Offset(0f, 0f),
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val center = Offset(
            x = size.center.x - ((size.center.x - origin.x) * (1f - progress)),
            y = size.center.y - ((size.center.y - origin.y) * (1f - progress)),
        )
        val radius = (kotlin.math.sqrt(
            size.height * size.height + size.width * size.width
        ) * .5f) * progress

        return Outline.Generic(
            Path().apply {
                addOval(
                    Rect(
                        center = center,
                        radius = radius
                    )
                )
            }
        )
    }
}