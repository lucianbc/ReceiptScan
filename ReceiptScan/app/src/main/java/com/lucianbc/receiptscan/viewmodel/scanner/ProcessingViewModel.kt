package com.lucianbc.receiptscan.viewmodel.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import com.lucianbc.receiptscan.util.map
import javax.inject.Inject

class ProcessingViewModel @Inject constructor(
    receiptScanner: ReceiptScanner
): ViewModel() {

    val scannerState = receiptScanner
        .state
        .toLiveData()
        .map { it.message }

    private val ReceiptScanner.State.message: String
        get() = when(this) {
            ReceiptScanner.State.Idle -> "Idle"
            ReceiptScanner.State.ReadingImage -> "Reading"
            ReceiptScanner.State.OCR -> "OCR"
            ReceiptScanner.State.Saving -> "Saving"
        }
}