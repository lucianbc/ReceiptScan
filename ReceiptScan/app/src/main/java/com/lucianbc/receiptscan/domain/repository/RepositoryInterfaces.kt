package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanAnnotations
import com.lucianbc.receiptscan.domain.model.ScanInfoBox

interface ImageRepository {
    fun saveImage(value: Bitmap): String
}

interface ReceiptDraftRepository {
    fun saveDraft(imagePath: String, metadata: ScanAnnotations): ReceiptDraft
}

class DummyImageRepo: ImageRepository {
    override fun saveImage(value: Bitmap): String {
        return "Dummy"
    }
}

class DummyReceiptDraftRepo: ReceiptDraftRepository {
    override fun saveDraft(imagePath: String, metadata: ScanAnnotations): ReceiptDraft {
        return ReceiptDraft(imagePath, metadata)
    }
}