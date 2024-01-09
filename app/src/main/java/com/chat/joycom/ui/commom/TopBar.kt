package com.chat.joycom.ui.commom

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
fun TopBarIcon(@DrawableRes drawableId: Int, onClick: () -> Unit) {
    Icon(
        painterResource(id = drawableId),
        "",
        modifier = Modifier
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
fun TopBarContactSearch(clickBack: () -> Unit) {
    val searchBgColor = if (isSystemInDarkTheme()) Color(0xFF202C33) else Color.LightGray
    var searchInput by remember {
        mutableStateOf("")
    }
    var keyType by remember {
        mutableStateOf(KeyboardType.Text)
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }
    val scope = rememberCoroutineScope()
    val res = LocalContext.current.resources
    val config = LocalConfiguration.current
    val screenWidth = TypedValueCompat.dpToPx(config.screenWidthDp.toFloat(), res.displayMetrics)
    val animationOffset by remember { mutableStateOf(Offset(screenWidth, 0f)) }
    val revealSize = remember { Animatable(0f) }
    LaunchedEffect(key1 = "reveal", block = {
        if (animationOffset.x > 0f) {
            revealSize.snapTo(0f)
            revealSize.animateTo(
                1f,
                tween(500)
            )
        } else revealSize.snapTo(1f)
    })

    JoyComTopBarSearchTheme {
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
                    TopBarIcon(drawableId = R.drawable.ic_arrow_back) {
                        scope.launch {
                            revealSize.snapTo(1f)
                            revealSize.animateTo(
                                0f,
                                tween(500)
                            )
                            awaitFrame()
                            clickBack.invoke()
                        }
                    }
                    DefaultInput(
                        inputText = searchInput,
                        onValueChange = { searchInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        hint = R.string.pls_input_name_or_phone_number,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyType),
                        singleLine = true
                    )
                    TopBarIcon(drawableId = if (keyType == KeyboardType.Text) R.drawable.ic_key_number else R.drawable.ic_keyboard) {
                        keyType = if (keyType == KeyboardType.Text) {
                            KeyboardType.Number
                        } else {
                            KeyboardType.Text
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                }
            },
            modifier = Modifier.clip(CirclePath(revealSize.value, animationOffset))
        )
    }
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