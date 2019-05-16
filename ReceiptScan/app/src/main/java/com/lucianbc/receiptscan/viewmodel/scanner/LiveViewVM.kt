package com.lucianbc.receiptscan.viewmodel.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import com.lucianbc.receiptscan.util.loge
import com.otaliastudios.cameraview.Frame
import javax.inject.Inject

class LiveViewVM @Inject constructor(
    private val receiptScanner: ReceiptScanner
): ViewModel() {
    val flash = MutableLiveData<Boolean>(false)

    val ocrOverlays = receiptScanner
        .ocrElements
        .toLiveData()

    fun toggleFlash() {
        flash.value = flash.value?.not()
    }

    fun processFrame(frame: Frame) {
        try {
            val fib = frame.toFirebaseImage()
            receiptScanner.processFrame(fib)
        } catch (e: IllegalArgumentException) {
            loge("Camera passed a frame with errors", e)
        }
    }

    private fun Frame.toFirebaseImage(): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(this.format)
            .setRotation(this.rotation.rotation())
            .setHeight(this.size.height)
            .setWidth(this.size.width)
            .build()
        return FirebaseVisionImage.fromByteArray(this.data, metadata)
    }

    private fun Int.rotation(): Int {
        return when (this) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }
}