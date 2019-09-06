package com.lucianbc.receiptscan.presentation.scanner

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.extract.ExtractUseCase
import com.lucianbc.receiptscan.infrastructure.ScannableFactory
import com.lucianbc.receiptscan.util.loge
import com.otaliastudios.cameraview.Frame
import javax.inject.Inject

class ViewfinderViewModel @Inject constructor(
    private val extractUseCase: ExtractUseCase,
    private val factory: ScannableFactory
) : ViewModel() {
    private val _flash = MutableLiveData(false)

    val flash: LiveData<Boolean>
        get() = _flash

    val ocrElements = extractUseCase.preview.toLiveData()

    fun toggleFlash() {
        _flash.value = flash.value?.not()
    }

    fun processFrame(frame: Frame) {
        try {
            factory
                .create(frame)
                .let(extractUseCase::feedPreview)
        } catch (e: IllegalArgumentException) {
            loge("Camera passed a frame with errors", e)
        }
    }

    /**
     * Test Only
     */
    fun processImage(image: Bitmap) {
        try {
            factory
                .create(image)
                .let(extractUseCase::feedPreview)
        } catch (e: IllegalArgumentException) {
            loge("Camera passed a frame with errors", e)
        }
    }
}