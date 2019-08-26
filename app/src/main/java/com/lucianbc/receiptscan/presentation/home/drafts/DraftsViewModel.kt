package com.lucianbc.receiptscan.presentation.home.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.drafts.DraftListItem
import com.lucianbc.receiptscan.domain.extract.ExtractUseCase
import com.lucianbc.receiptscan.domain.extract.State
import com.lucianbc.receiptscan.domain.drafts.DraftsUseCase
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    draftsUseCase: DraftsUseCase,
    extractUseCase: ExtractUseCase
) : ViewModel() {

    val drafts: LiveData<List<DraftListItem>> = draftsUseCase.list().toLiveData()
    val scanningState: LiveData<String> =
        extractUseCase.state
            .map { it.message }
            .toLiveData()

    private val State.message: String
        get() = when (this) {
            State.Processing -> "Processing"
            State.Idle -> "Idle"
            is State.Error -> "Error ${this.err.message}"
        }
}