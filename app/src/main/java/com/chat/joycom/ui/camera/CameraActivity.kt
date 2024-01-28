package com.chat.joycom.ui.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.TakePictureCamera
import com.chat.joycom.ui.theme.JoyComTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, CameraActivity::class.java)
            )
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isRecordVideo by remember {
                mutableStateOf(false)
            }
            val actionSizeAnimate by animateFloatAsState(
                targetValue = if (isRecordVideo) .35f else .7f,
                label = ""
            )
            val recordOffsetDpAnimate by animateDpAsState(
                targetValue = if (isRecordVideo) 0.dp else (-100).dp,
                label = ""
            )
            val shotOffsetDpAnimate by animateDpAsState(
                targetValue = if (isRecordVideo) (100).dp else 0.dp,
                label = ""
            )
            val permissionState =
                rememberMultiplePermissionsState(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        listOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                        )
                    } else {
                        listOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }
                )
            JoyComTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .statusBarsPadding()
                ) {
                    if (permissionState.allPermissionsGranted) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(.8f)
                                    .align(Alignment.TopCenter)
                            ) {
                                TakePictureCamera(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                                )
                                Icon(
                                    painterResource(id = R.drawable.ic_add),
                                    "",
                                    Modifier
                                        .padding(top = 10.dp, start = 10.dp)
                                        .rotate(45f)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            finish()
                                        },
                                    tint = Color.White
                                )
                                if (isRecordVideo) {
                                    Text(
                                        "00:00",
                                        modifier = Modifier
                                            .align(Alignment.TopCenter)
                                            .padding(top = 10.dp)
                                            .background(
                                                Color.DarkGray.copy(.5f),
                                                RoundedCornerShape(40.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        fontSize = 22.sp,
                                        color = Color.White
                                    )
                                }
                                Icon(
                                    painterResource(id = R.drawable.ic_image),
                                    "",
                                    modifier = Modifier
                                        .padding(start = 10.dp, bottom = 10.dp)
                                        .size(50.dp)
                                        .align(Alignment.BottomStart)
                                        .background(Color.DarkGray.copy(.5f), CircleShape)
                                        .padding(10.dp),
                                    tint = Color.White
                                )
                                Icon(
                                    painterResource(id = R.drawable.ic_flip_camera),
                                    "",
                                    modifier = Modifier
                                        .padding(end = 10.dp, bottom = 10.dp)
                                        .size(50.dp)
                                        .align(Alignment.BottomEnd)
                                        .background(Color.DarkGray.copy(.5f), CircleShape)
                                        .padding(10.dp),
                                    tint = Color.White
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(70.dp)
                                        .align(Alignment.BottomCenter)
                                        .border(4.dp, Color.White, CircleShape)
                                        .clickable {

                                        }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxSize(actionSizeAnimate)
                                            .background(Color.White, CircleShape)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(.2f)
                                    .align(Alignment.BottomCenter)

                            ) {
                                Text(
                                    text = stringResource(id = R.string.record),
                                    modifier = Modifier
                                        .offset(x = recordOffsetDpAnimate)
                                        .clickable {
                                            isRecordVideo = true
                                        }
                                        .align(Alignment.TopCenter)
                                        .background(
                                            Color.DarkGray.copy(if (isRecordVideo) .5f else .0f),
                                            RoundedCornerShape(40.dp)
                                        )
                                        .padding(horizontal = 20.dp, vertical = 10.dp),
                                    Color.White
                                )
                                Text(
                                    text = stringResource(id = R.string.shot),
                                    modifier = Modifier
                                        .offset(x = shotOffsetDpAnimate)
                                        .clickable {
                                            isRecordVideo = false
                                        }
                                        .align(Alignment.TopCenter)
                                        .background(
                                            Color.DarkGray.copy(if (!isRecordVideo) .5f else .0f),
                                            RoundedCornerShape(40.dp)
                                        )
                                        .padding(horizontal = 20.dp, vertical = 10.dp),
                                    Color.White
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()

                        ) {

                        }
                    }
                }
            }
        }
    }
}