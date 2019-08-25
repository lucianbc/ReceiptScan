package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import com.lucianbc.receiptscan.infrastructure.dao.Converters
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class DraftsRepositoryImpl @Inject constructor(
    private val draftDao: DraftDao,
    private val imagesDao: ImagesDao
) : DraftsRepository {
    override fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date) =
        draftDao.getTextReceiptsBetween(
            Converters.toTimestamp(firstDate)!!,
            Converters.toTimestamp(lastDate)!!
        )

    override fun getImageReceiptsBetween(firstDate: Date, lastDate: Date) =
        draftDao.getImageReceiptsBetween(
            Converters.toTimestamp(firstDate)!!,
            Converters.toTimestamp(lastDate)!!
        )

    override fun validate(draftId: Long) = draftDao.validate(draftId)

    override fun insert(draft: Draft) = draftDao.insert(draft.entity())

    override fun update(draft: Draft) = draftDao.update(draft.entity())

    override fun update(product: Product) = draftDao.insert(product)

    override fun getImage(id: Long) =
        draftDao
            .getImagePath(id)
            .map { imagesDao.readImage(it) }

    override fun getAllItems(): Flowable<List<ListDraftsUseCase.DraftItem>> =
        draftDao.getDraftItems()

    override fun getAllReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>> =
        draftDao.getReceiptItems()

    override fun getDraft(id: Long): Flowable<DraftWithProducts> =
        draftDao.getDraft(id)

    override fun insert(product: Product): Single<Long> =
        draftDao.insert(product)

    override fun getOcrElements(draftId: Long): Flowable<List<OcrElement>> =
        draftDao.getOcrElements(draftId)

    override fun delete(draftId: Long) = draftDao.delete(draftId)

    override fun deleteProduct(id: Long) = draftDao.deleteProduct(id)

    override fun getReceipt(id: Long) = draftDao.getReceipt(id)

    override fun getExported(id: Long): Single<ExportedReceipt> = draftDao.getExported(id)
}