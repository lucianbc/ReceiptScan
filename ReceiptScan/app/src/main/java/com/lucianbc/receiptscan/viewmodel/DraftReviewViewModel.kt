package com.lucianbc.receiptscan.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.dao.ioLiveData
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.repository.ReceiptDraftRepository
import javax.inject.Inject

class DraftReviewViewModel @Inject constructor(
    private val receiptDraftRepository: ReceiptDraftRepository
): ViewModel()  {
    lateinit var image: LiveData<Bitmap>
    lateinit var data: LiveData<ReceiptDraft>

    fun initialize(draftId: ID) {
        val draft = receiptDraftRepository.loadDraft(draftId)
        image = draft
            .flatMap { receiptDraftRepository.loadImage(it.imagePath) }
            .ioLiveData()
        data = draft.ioLiveData()
    }
}
