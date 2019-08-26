package com.lucianbc.receiptscan.presentation.home.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.extract.ExtractUseCase
import com.lucianbc.receiptscan.domain.extract.State
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.util.logd
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    listDraftsUseCase: ListDraftsUseCase,
    extractUseCase: ExtractUseCase
) : ViewModel() {

    val drafts: LiveData<List<ListDraftsUseCase.DraftItem>> = listDraftsUseCase.execute().toLiveData()
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