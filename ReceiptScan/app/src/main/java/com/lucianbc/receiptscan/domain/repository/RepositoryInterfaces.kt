package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanInfoBox

interface ImageRepository {
    fun saveImage(value: Bitmap): String
}

interface ReceiptDraftRepository {
    fun saveDraft(imagePath: String, metadata: Collection<ScanInfoBox>): ReceiptDraft
}