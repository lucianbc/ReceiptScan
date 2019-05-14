package com.lucianbc.receiptscan.viewmodel.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import javax.inject.Inject

class ProcessingViewModel @Inject constructor(
    receiptScanner: ReceiptScanner
): ViewModel() {

    val scannerState = receiptScanner
        .state
        .toLiveData()
        .map { it.message }

    private fun <X, Y> LiveData<X>.map(func: (X) -> Y): LiveData<Y> {
        return Transformations.map(this, func)
    }

    private val ReceiptScanner.State.message: String
        get() = when(this) {
            ReceiptScanner.State.Idle -> "Idle"
            ReceiptScanner.State.ReadingImage -> "Reading"
            ReceiptScanner.State.OCR -> "OCR"
            ReceiptScanner.State.Saving -> "Saving"
        }
}