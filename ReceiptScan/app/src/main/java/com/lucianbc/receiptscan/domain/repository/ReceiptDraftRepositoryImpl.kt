package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.dao.ReceiptDraftDao
import com.lucianbc.receiptscan.domain.dao.Response
import com.lucianbc.receiptscan.domain.dao.ScannedImageDao
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.util.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptDraftRepositoryImpl @Inject constructor(
    private val receiptScansDao: ScannedImageDao,
    private val receiptDraftDao: ReceiptDraftDao
): ReceiptDraftRepository {
    override fun saveAnnotation(annotation: ScanInfoBox) {
        receiptDraftDao.insertAnnotation(annotation)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private var lastReceiptCache: Optional<DraftWithImage> = None

    override fun saveDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft {
        val result = persistDraft(receiptDraft, image)
        lastReceiptCache = Just(result to image)
        return result
    }

    override fun loadDraft(id: ID): Response<ReceiptDraft> {
        val cache = lastReceiptCache
        return when {
            cache is Just && cache.value.draft.id == id ->
                Observable
                    .just(cache.value.draft)
                    .toFlowable(BackpressureStrategy.LATEST)
            else -> receiptDraftDao.find(id)
        }
    }

    override fun loadImage(imagePath: String): Response<Bitmap> {
        val cache = lastReceiptCache
        return when {
            cache is Just && cache.value.draft.imagePath == imagePath ->
                Observable
                    .just(cache.value.image)
                    .toFlowable(BackpressureStrategy.LATEST)
            else ->
                Observable
                    .just(receiptScansDao.readImage(imagePath))
                    .toFlowable(BackpressureStrategy.LATEST)
        }
    }

    private fun persistDraft(receiptDraft: ReceiptDraft, image: Bitmap): ReceiptDraft {
        val path = receiptScansDao.saveImage(image)
        receiptDraft.imagePath = path
        val draftId = receiptDraftDao.insert(receiptDraft)
        receiptDraft.id = draftId
        return receiptDraft
    }
}