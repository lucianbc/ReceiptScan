package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.dao.Response
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft

interface ReceiptDraftRepository {
    fun saveDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft
    fun loadDraftWithImage(id: ID): Response<DraftWithImage>
}




typealias DraftWithImage = Pair<ReceiptDraft, Bitmap>

val DraftWithImage.image
    get() = this.second

val DraftWithImage.draft
    get() = this.first