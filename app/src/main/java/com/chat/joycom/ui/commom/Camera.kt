package com.chat.joycom.ui.commom

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

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
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                val maineExecutor = ContextCompat.getMainExecutor(context)
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

                        val analyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .apply {
                                setAnalyzer(maineExecutor, QrcodeAnalyzer {
                                    // TODO: callback to top
                                })
                            }

                        var camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            analyzer
                        )
                    }, maineExecutor)
                }
            }
        }
    )
}

@Composable
fun TakePictureCamera(modifier: Modifier = Modifier) {
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
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                val maineExecutor = ContextCompat.getMainExecutor(context)

                val name = SimpleDateFormat(System.currentTimeMillis().toString(), Locale.US)
                    .format(System.currentTimeMillis())

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
                    }
                }
                val outputOptions = ImageCapture.OutputFileOptions
                    .Builder(context.contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues)
                    .build()

                post {
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview: Preview = Preview.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                            .build()

                        val cameraSelector: CameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        preview.setSurfaceProvider(this.surfaceProvider)

                        val imageCapture = ImageCapture.Builder()
                            .setTargetRotation(this.display.rotation)
                            .build()
                            .apply {
                                takePicture(outputOptions, maineExecutor, object : ImageCapture.OnImageSavedCallback{
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        Timber.d("TakePictureCamera takePicture onImageSaved => ${outputFileResults.savedUri}")
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        exception.printStackTrace()
                                        Timber.d("TakePictureCamera takePicture error => ${exception.message}")
                                    }
                                })
                            }

                        var camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            imageCapture,
                            preview,
                        )
                    }, maineExecutor)
                }
            }
        }
    )
}