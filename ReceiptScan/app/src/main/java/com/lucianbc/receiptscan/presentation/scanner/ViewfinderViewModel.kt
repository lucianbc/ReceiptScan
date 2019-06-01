package com.lucianbc.receiptscan.presentation.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.usecase.LiveViewUseCase
import com.lucianbc.receiptscan.domain.usecase.ScanUseCase
import com.lucianbc.receiptscan.infrastructure.OcrElementsProducersFactory
import com.lucianbc.receiptscan.util.loge
import com.otaliastudios.cameraview.Frame
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ViewfinderViewModel @Inject constructor(
    private val liveViewUseCase: LiveViewUseCase,
    private val factory: OcrElementsProducersFactory
): ViewModel() {
    private val _flash = MutableLiveData<Boolean>(false)

    val flash: LiveData<Boolean>
        get() = _flash

    val ocrElements = liveViewUseCase.ocrElements.toLiveData()

    fun toggleFlash() {
        _flash.value = flash.value?.not()
    }

    fun processFrame(frame: Frame) {
        try {
            val ocrProducer = factory.simple(frame)
            liveViewUseCase.scanFrame(ocrProducer)
        } catch (e: IllegalArgumentException) {
            loge("Camera passed a frame with errors", e)
        }
    }
}