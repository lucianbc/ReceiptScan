package com.lucianbc.receiptscan.presentation.scanner

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.viewfinder.LiveViewUseCase
import com.lucianbc.receiptscan.infrastructure.ScannableFactory
import com.lucianbc.receiptscan.util.loge
import com.otaliastudios.cameraview.Frame
import javax.inject.Inject

class ViewfinderViewModel @Inject constructor(
    private val liveViewUseCase: LiveViewUseCase,
    private val factory: ScannableFactory
) : ViewModel() {
    private val _flash = MutableLiveData(false)

    val flash: LiveData<Boolean>
        get() = _flash

    val ocrElements = liveViewUseCase.ocrElements.toLiveData()

    fun toggleFlash() {
        _flash.value = flash.value?.not()
    }

    fun processFrame(frame: Frame) {
        try {
            val ocrProducer = factory.create(frame)
            liveViewUseCase.scan(ocrProducer)
        } catch (e: IllegalArgumentException) {
            loge("Camera passed a frame with errors", e)
        }
    }

    fun processImage(image: Bitmap) {
        try {
            val ocrProducer = factory.create(image)
            liveViewUseCase.scan(ocrProducer)
        } catch (e: IllegalArgumentException) {
            loge("Camera passed a frame with errors", e)
        }
    }
}