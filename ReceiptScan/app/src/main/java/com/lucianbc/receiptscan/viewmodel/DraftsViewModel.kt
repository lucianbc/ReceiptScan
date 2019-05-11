package com.lucianbc.receiptscan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.dao.AppDatabase
import com.lucianbc.receiptscan.domain.model.Drafts
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    private val database: AppDatabase
): ViewModel() {
    val drafts: LiveData<Drafts> =
        database.receiptDraftDao().findAllWithAnnotations()
}