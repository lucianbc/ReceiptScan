package com.lucianbc.receiptscan.presentation.home.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.DraftItem
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    useCase: ListDraftsUseCase
): ViewModel() {
    val drafts: LiveData<List<DraftItem>> = useCase.execute().toLiveData()
}