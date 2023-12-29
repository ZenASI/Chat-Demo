package com.chat.joycom.ui.commom

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import timber.log.Timber
import java.nio.ByteBuffer

class QrcodeAnalyzer(private val onQrCodeResult: (text: String) -> Unit) : ImageAnalysis.Analyzer {

    private val reader = MultiFormatReader().apply {
        val map = mapOf(
            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE)
        )
        setHints(map)
    }

    override fun analyze(image: ImageProxy) {
        try {
            val data = image.planes[0].buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                data,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = reader.decode(binaryBitmap)
                onQrCodeResult.invoke(result.text)
                Timber.d(result.text)
            } catch (e: NotFoundException) {
//                e.printStackTrace()
//                Timber.d("QrcodeAnalyzer decode error => ${e.message}")
            }
            image.close()
        } catch (e: Throwable) {
//            e.printStackTrace()
            Timber.d("QrcodeAnalyzer error => ${e.message}")
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }
}