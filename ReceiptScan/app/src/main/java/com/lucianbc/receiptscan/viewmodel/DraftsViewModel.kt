package com.lucianbc.receiptscan.viewmodel

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.dao.ReceiptDraftDao
import com.lucianbc.receiptscan.domain.dao.ioLiveData
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    receiptDraftDao: ReceiptDraftDao
): ViewModel() {
    val drafts = receiptDraftDao
        .findAll()
        .ioLiveData()

}