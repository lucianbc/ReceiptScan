package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.scanner.DraftValue
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class DraftsRepositoryImpl @Inject constructor(
    private val draftDao: DraftDao,
    private val imagesDao: ImagesDao
) : DraftsRepository {
    override fun update(receiptId: Long, products: List<Product>) =
        Single.fromCallable { draftDao.updateProducts(receiptId, products) }

    override fun validate(draftId: Long) = draftDao.validate(draftId)

    override fun create(value: DraftValue): Observable<Long> =
        Observable
            .fromCallable { imagesDao.saveImage(value.image) }
            .flatMapSingle { draftDao.insert(value.receipt(it)) }
            .flatMapSingle { receiptId ->
                draftDao.insertProducts(value.products(receiptId)).map { receiptId }
            }
            .flatMapSingle { receiptId ->
                val els = value.elements(receiptId)
                draftDao.insert(els).map { receiptId }
            }

    override fun update(draft: Draft) = draftDao.insert(draft.receipt())

    override fun getImage(id: Long) =
        draftDao
            .getImagePath(id)
            .map { imagesDao.readImage(it) }

    override fun getAllItems(): Flowable<List<ListDraftsUseCase.DraftItem>> =
        draftDao.getDraftItems()

    override fun getAllReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>> =
        draftDao.getReceiptItems()

    override fun getReceipt(id: Long): Flowable<DraftWithProducts> =
        draftDao.getReceipt(id)

    override fun getOcrElements(draftId: Long): Flowable<List<OcrElement>> =
        draftDao.getOcrElements(draftId)

    override fun delete(draftId: Long) = draftDao.delete(draftId)

    override fun saveReceipt(data: DraftWithProducts) = draftDao.updateReceipt(data)
}