package com.lucianbc.receiptscan.domain.export

import io.reactivex.Completable
import io.reactivex.Flowable

interface ExportUseCase {
    fun list(): Flowable<List<Export>>
    fun newExport(manifest: Session): Completable
    fun markAsFinished(notification: FinishedNotification): Completable
}