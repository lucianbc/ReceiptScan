package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.dao.Response
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft

interface ReceiptDraftRepository {
    fun saveDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft
    fun loadDraft(id: ID): Response<ReceiptDraft>
    fun loadImage(imagePath: String): Response<Bitmap>
}

typealias DraftWithImage = Pair<ReceiptDraft, Bitmap>

val DraftWithImage.image
    get() = this.second

val DraftWithImage.draft
    get() = this.first