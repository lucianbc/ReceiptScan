package com.lucianbc.receiptscan.presentation.home.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.DraftItem
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ScanUseCase
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    listDraftsUseCase: ListDraftsUseCase,
    scanUseCase: ScanUseCase
): ViewModel() {
    val drafts: LiveData<List<DraftItem>> = listDraftsUseCase.execute().toLiveData()
    val scanningState: LiveData<String> =
        scanUseCase.state
            .map { it.message }
            .toLiveData()

    private val ScanUseCase.State.message: String
        get() = when (this) {
            ScanUseCase.State.OCR -> "Ocr"
            ScanUseCase.State.Tagging -> "Tagging"
            ScanUseCase.State.Saving -> "Saving"
            ScanUseCase.State.Idle -> "Idle"
            ScanUseCase.State.Error -> "Error"
        }
}