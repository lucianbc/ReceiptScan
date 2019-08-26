package com.lucianbc.receiptscan.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.extract.ExtractUseCase
import com.lucianbc.receiptscan.domain.extract.State
import com.lucianbc.receiptscan.util.logd
import javax.inject.Inject

class ProcessingViewModel @Inject constructor(
    extractUseCase: ExtractUseCase
) : ViewModel() {

    val scanningState = extractUseCase
        .state
        .map { it.message }
        .toLiveData()

    private val State.message: String
        get() = when (this) {
            State.Idle -> "Idle"
            State.Processing -> "Processing"
            is State.Error -> "Error"
        }
}