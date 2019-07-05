package com.lucianbc.receiptscan.presentation.home.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import javax.inject.Inject

class ReceiptsViewModel @Inject constructor(
    listReceiptsUseCase: ListReceiptsUseCase
) : ViewModel() {
    val receipts = listReceiptsUseCase.execute().toLiveData()
}