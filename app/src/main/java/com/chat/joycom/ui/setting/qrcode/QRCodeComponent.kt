package com.chat.joycom.ui.setting.qrcode

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.view.PreviewView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber

@Composable
fun MyQrcode() {
    Box(modifier = Modifier.fillMaxSize()) {

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQrcode() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            QrcodeCamera()
        } else {
            // TODO: check permission
        }
    }
}

@Composable
fun QrcodeTabRow(currentScene: QRCodeScene, onClick: (Int) -> Unit) {
    val tabList = QRCodeScene.values().toList()
    val unSelectColor = if (isSystemInDarkTheme()) Color(0xFF8696A0) else Color(0XFFB2D9D2)
    val selectColor = if (isSystemInDarkTheme()) Color(0xFF00A884) else Color.White
    TabRow(
        selectedTabIndex = currentScene.ordinal,
        divider = { },
        indicator = { tabPositions ->
            if (currentScene.ordinal < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[currentScene.ordinal]),
//                    1.dp,
                    color = selectColor,
                )
            }
        },
    ) {
        tabList.forEachIndexed { index, item ->
            Tab(
                selected = currentScene.ordinal == index,
                onClick = { onClick.invoke(index) },
                selectedContentColor = selectColor,
                unselectedContentColor = unSelectColor
            ) {
                Text(text = stringResource(id = item.title), modifier = Modifier.padding(15.dp))
            }
        }
    }
}

@Composable
fun QrcodeCamera() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    DisposableEffect(key1 = cameraProviderFuture) {
        onDispose {
            try {
                cameraProviderFuture.get().unbindAll()

            } catch (e: Throwable) {
                e.printStackTrace()
                Timber.d(e.message)
            }
        }
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                post {
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview: Preview = Preview.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                            .build()

                        val cameraSelector: CameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        preview.setSurfaceProvider(this.surfaceProvider)

                        var camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview
                        )
                    }, ContextCompat.getMainExecutor(context))
                }
            }
        }
    )
}