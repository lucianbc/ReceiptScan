package com.lucianbc.receiptscan.infrastructure.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.extract.*
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.infrastructure.entities.ProductEntity
import com.lucianbc.receiptscan.infrastructure.entities.ReceiptEntity
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import com.lucianbc.receiptscan.infrastructure.entities.OcrElementEntity
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class ExtractRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val imagesDao: ImagesDao
) : ExtractRepository {
    override fun persist(draft: Draft, image: Bitmap): Single<DraftId> {
        return saveImageComputation(image)
            .saveDraft(draft)
            .saveProducts(draft)
            .saveOcrElements(draft)
    }

    private fun saveImageComputation(image: Bitmap) =
        Single.fromCallable { imagesDao.saveWithPath(image) }

    private fun Single<ImagePath>.saveDraft(draft: Draft) =
        this.flatMap { draft.persisted(it).let(appDao::insert) }

    private fun Single<DraftId>.saveProducts(draft: Draft) =
        this.flatMap { draftId ->
            appDao
                .insertProducts(draft.products.persisted(draftId))
                .map { draftId }
        }

    private fun Single<DraftId>.saveOcrElements(draft: Draft) =
        this.flatMap { draftId ->
            draft.ocrElements.persistedOcr(draftId)
                .let(appDao::insert)
                .map { draftId }
        }
}

private fun Draft.persisted(imagePath: ImagePath) =
    ReceiptEntity(
        imagePath.value,
        merchantName,
        date,
        total,
        currency,
        category,
        Date(),
        true
    )

private fun List<Product>.persisted(parentId: DraftId) =
    this.map {
        ProductEntity(
            it.name, it.price, receiptId = parentId
        )
    }

private fun List<OcrElement>.persistedOcr(parentId: DraftId) =
    this.map {
        OcrElementEntity(
            it.text,
            it.top,
            it.left,
            it.bottom,
            it.right,
            receiptId = parentId
        )
    }