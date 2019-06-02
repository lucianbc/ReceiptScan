package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.CreateDraftCommand
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
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
            .map { saveAnnotations(command, it) }


    private fun saveDraft(filename: String): Single<Long> {
        val draft = defaultDraft(filename)
        return draftDao.insert(draft)
    }

    private fun saveAnnotations(command: CreateDraftCommand, draftId: Long): Long {
        command.annotations.forEach { it.draftId = draftId }
        draftDao.insert(command.annotations.toList())
        return draftId
    }

    private fun defaultDraft(filename: String) =
        Draft(filename, null, null, null, null, true, Date())
}