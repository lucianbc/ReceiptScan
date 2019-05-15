package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.dao.ReceiptDraftDao
import com.lucianbc.receiptscan.domain.dao.ScannedImageDao
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.util.Just
import com.lucianbc.receiptscan.util.None
import com.lucianbc.receiptscan.util.Optional
import javax.inject.Inject

interface ReceiptDraftRepository {
    fun saveDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft
    fun loadDraftWithImage(id: ID): DraftWithImage
}




typealias DraftWithImage = Pair<ReceiptDraft, Bitmap>

val DraftWithImage.image
    get() = this.second

val DraftWithImage.draft
    get() = this.first