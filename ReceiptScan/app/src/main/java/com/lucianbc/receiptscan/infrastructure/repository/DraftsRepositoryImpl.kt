package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.CreateDraftCommand
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftItem
import com.lucianbc.receiptscan.domain.model.ReceiptDraftWithProducts
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class DraftsRepositoryImpl @Inject constructor(
    private val draftDao: DraftDao,
    private val imagesDao: ImagesDao
) : DraftsRepository {
    override fun create(command: CreateDraftCommand): Observable<Long> =
        Observable
            .fromCallable { imagesDao.saveImage(command.image) }
            .flatMapSingle { saveDraft(it) }
            .flatMapSingle { saveAnnotations(command, it) }

    override fun getImage(id: Long) =
        draftDao
            .getImagePath(id)
            .map { imagesDao.readImage(it) }


    override fun getAllItems(): Flowable<List<DraftItem>> =
        draftDao.getDraftItems()

    override fun getReceipt(id: Long): Flowable<ReceiptDraftWithProducts> =
        draftDao.getReceipt()

    override fun getAnnotations(draftId: Long): Flowable<List<Annotation>> =
        draftDao.getAnnotations(draftId)

    override fun delete(draftId: Long) = draftDao.delete(draftId)

    private fun saveDraft(filename: String): Single<Long> {
        val draft = defaultDraft(filename)
        return draftDao.insert(draft)
    }

    private fun saveAnnotations(command: CreateDraftCommand, draftId: Long): Single<Long> {
        command.annotations.forEach { it.draftId = draftId }
        return draftDao
            .insert(command.annotations.toList())
            .map { draftId }
    }

    private fun defaultDraft(filename: String) =
        Draft(filename, null, null, null, null, true, Date())
}