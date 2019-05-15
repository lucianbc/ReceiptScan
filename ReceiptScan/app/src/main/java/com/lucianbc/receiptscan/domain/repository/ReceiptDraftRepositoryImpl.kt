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

class ReceiptDraftRepositoryImpl @Inject constructor(
    private val receiptScansDao: ScannedImageDao,
    private val receiptDraftDao: ReceiptDraftDao
): ReceiptDraftRepository {
    private var lastReceiptCache: Optional<DraftWithImage> = None

    override fun saveDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft {
        val result = persistDraft(receiptDraft, image)
        lastReceiptCache = Just(result to image)
        return result
    }

    override fun loadDraftWithImage(id: ID): DraftWithImage {
        val cache = lastReceiptCache
        return when {
            cache is Just && cache.value.draft.id == id -> cache.value
            else -> readFromStorage(id)
        }
    }

    private fun readFromStorage(id: ID): DraftWithImage {
        val draft = receiptDraftDao.find(id)
        val image = receiptScansDao.readImage(draft.imagePath)
        return draft to image
    }

    private fun persistDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft {
        val path = receiptScansDao.saveImage(image)
        receiptDraft.imagePath = path
        val draftId = receiptDraftDao.insert(receiptDraft)
        receiptDraft.id = draftId
        return receiptDraft
    }
}