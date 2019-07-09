package com.lucianbc.receiptscan.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.scanner.ScanUseCase
import javax.inject.Inject

class ProcessingViewModel @Inject constructor(
    scanUseCase: ScanUseCase
) : ViewModel() {

    val scanningState = scanUseCase
        .state
        .map { it.message }
        .toLiveData()

    private val ScanUseCase.State.message: String
        get() = when (this) {
            ScanUseCase.State.OCR -> "Ocr"
            ScanUseCase.State.Saving -> "Saving"
            ScanUseCase.State.Idle -> "Idle"
            ScanUseCase.State.Error -> "Error"
        }
}